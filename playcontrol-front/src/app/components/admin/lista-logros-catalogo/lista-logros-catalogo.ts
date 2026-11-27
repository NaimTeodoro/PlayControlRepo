import { Component, computed, effect, inject, signal, viewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { CONDICIONES_LOGRO, Logro, TIPOS_LOGRO } from '../../../models/logro';
import { LogroService } from '../../../services/logro-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// Catálogo de logros (ADMIN): tabla con búsqueda, editar y eliminar
@Component({
  selector: 'app-lista-logros-catalogo',
  standalone: false,
  templateUrl: './lista-logros-catalogo.html',
  styleUrl: './lista-logros-catalogo.css',
})
export class ListaLogrosCatalogo {
  private logroService = inject(LogroService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  private logros = signal<Logro[]>([]);
  readonly busqueda = signal('');
  // Logros cuya imagen no cargó (URL inválida): se muestra el icono genérico
  readonly imagenesRotas = signal<Set<number>>(new Set());

  readonly columnas = ['logro', 'tipo', 'condicion', 'puntos', 'estado', 'acciones'];
  readonly datos = new MatTableDataSource<Logro>([]);
  private paginador = viewChild(MatPaginator);

  readonly filtrados = computed(() => {
    const texto = this.busqueda().trim().toLowerCase();
    return this.logros().filter(
      (l) => !texto || `${l.nombreLogro} ${l.descripcion ?? ''}`.toLowerCase().includes(texto),
    );
  });

  constructor() {
    effect(() => (this.datos.data = this.filtrados()));
    effect(() => {
      const paginador = this.paginador();
      if (paginador) this.datos.paginator = paginador;
    });
    this.cargar();
  }

  private cargar(): void {
    this.cargando.set(true);
    this.logroService.listar().subscribe({
      next: (lista) => {
        this.logros.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  tipo(l: Logro): string {
    return TIPOS_LOGRO.find((t) => t.valor === l.tipo)?.etiqueta ?? '—';
  }

  condicion(l: Logro): string {
    const etiqueta = CONDICIONES_LOGRO.find((c) => c.valor === l.condicionTipo)?.etiqueta;
    if (!etiqueta) return l.condicion ?? '—';
    return l.valorCondicion != null ? `${etiqueta}: ${l.valorCondicion}` : etiqueta;
  }

  imagenRota(id: number): void {
    this.imagenesRotas.update((s) => new Set(s).add(id));
  }

  buscar(evento: Event): void {
    this.busqueda.set((evento.target as HTMLInputElement).value);
  }

  eliminar(l: Logro): void {
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Eliminar logro?',
          mensaje: `Se eliminará "${l.nombreLogro}" del catálogo. Si los jugadores ya tienen progreso en él, el sistema podría no permitirlo; en ese caso desactívalo.`,
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.logroService.eliminar(l.idLogro as number).subscribe({
          next: () => {
            this.notificacion.exito('Logro eliminado');
            this.cargar();
          },
          error: (err) =>
            this.notificacion.error(err, 'No se pudo eliminar: el logro está en uso. Desactívalo.'),
        });
      });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
