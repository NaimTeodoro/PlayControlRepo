import { Component, inject, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Plataforma } from '../../../models/plataforma';
import { NotificacionService } from '../../../services/notificacion-service';
import { PlataformaService } from '../../../services/plataforma-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// Icono según el tipo de plataforma
const ICONOS: Record<string, string> = {
  PC: 'computer',
  CONSOLA: 'sports_esports',
  MOVIL: 'smartphone',
};

// Plataformas (ADMIN): tabla con editar y eliminar
@Component({
  selector: 'app-lista-plataformas',
  standalone: false,
  templateUrl: './lista-plataformas.html',
  styleUrl: './lista-plataformas.css',
})
export class ListaPlataformas {
  private plataformaService = inject(PlataformaService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  readonly plataformas = signal<Plataforma[]>([]);
  readonly columnas = ['plataforma', 'tipo', 'sistema', 'desarrollador', 'estado', 'acciones'];

  constructor() {
    this.cargar();
  }

  icono(tipo?: string): string {
    return (tipo && ICONOS[tipo]) || 'devices_other';
  }

  private cargar(): void {
    this.cargando.set(true);
    this.plataformaService.listar().subscribe({
      next: (lista) => {
        this.plataformas.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  eliminar(p: Plataforma): void {
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Eliminar plataforma?',
          mensaje: `Se eliminará "${p.nombrePlataforma}". Si tiene juegos asociados, el sistema no lo permitirá.`,
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.plataformaService.eliminar(p.idPlataforma as number).subscribe({
          next: () => {
            this.notificacion.exito('Plataforma eliminada');
            this.cargar();
          },
          error: (err) =>
            this.notificacion.error(err, 'No se pudo eliminar: la plataforma está en uso'),
        });
      });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
