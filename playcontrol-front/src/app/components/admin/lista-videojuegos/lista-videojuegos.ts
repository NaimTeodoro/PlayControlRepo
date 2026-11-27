import { Component, computed, effect, inject, signal, viewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { forkJoin } from 'rxjs';
import { Videojuego } from '../../../models/videojuego';
import { Plataforma } from '../../../models/plataforma';
import { VideojuegoService } from '../../../services/videojuego-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { PlataformaService } from '../../../services/plataforma-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// Catálogo de juegos (ADMIN): tabla con búsqueda, editar y eliminar
@Component({
  selector: 'app-lista-videojuegos',
  standalone: false,
  templateUrl: './lista-videojuegos.html',
  styleUrl: './lista-videojuegos.css',
})
export class ListaVideojuegos {
  private videojuegoService = inject(VideojuegoService);
  private plataformaService = inject(PlataformaService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  private juegos = signal<Videojuego[]>([]);
  private plataformas = signal<Plataforma[]>([]);
  readonly busqueda = signal('');

  readonly columnas = ['juego', 'plataforma', 'genero', 'pegi', 'sesion', 'estado', 'acciones'];
  readonly datos = new MatTableDataSource<Videojuego>([]);
  private paginador = viewChild(MatPaginator);

  private nombresPlataforma = computed(
    () => new Map(this.plataformas().map((p) => [p.idPlataforma, p.nombrePlataforma])),
  );

  readonly filtrados = computed(() => {
    const texto = this.busqueda().trim().toLowerCase();
    return this.juegos().filter(
      (j) =>
        !texto ||
        `${j.nombreJuego} ${j.genero ?? ''} ${j.desarrollador ?? ''}`.toLowerCase().includes(texto),
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
    forkJoin({
      juegos: this.videojuegoService.listar(),
      plataformas: this.plataformaService.listar(),
    }).subscribe({
      next: ({ juegos, plataformas }) => {
        this.juegos.set(juegos);
        this.plataformas.set(plataformas);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  plataforma(j: Videojuego): string {
    return this.nombresPlataforma().get(j.idPlataforma) ?? '—';
  }

  buscar(evento: Event): void {
    this.busqueda.set((evento.target as HTMLInputElement).value);
  }

  eliminar(j: Videojuego): void {
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Eliminar juego?',
          mensaje: `Se eliminará "${j.nombreJuego}" del catálogo. Si tiene sesiones registradas, el sistema no lo permitirá.`,
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.videojuegoService.eliminar(j.idVideojuego as number).subscribe({
          next: () => {
            this.notificacion.exito('Videojuego eliminado');
            this.cargar();
          },
          error: (err) => this.notificacion.error(err, 'No se pudo eliminar: el juego está en uso'),
        });
      });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
