import { Component, inject, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Dispositivo } from '../../../models/dispositivo';
import { AuthService } from '../../../services/auth-service';
import { DispositivoService } from '../../../services/dispositivo-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// Icono según el tipo de dispositivo
export const ICONOS_DISPOSITIVO: Record<string, string> = {
  PC: 'computer',
  MOVIL: 'smartphone',
  CONSOLA: 'sports_esports',
};

@Component({
  selector: 'app-lista-dispositivos',
  standalone: false,
  templateUrl: './lista-dispositivos.html',
  styleUrl: './lista-dispositivos.css',
})
export class ListaDispositivos {
  private auth = inject(AuthService);
  private dispositivoService = inject(DispositivoService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  readonly dispositivos = signal<Dispositivo[]>([]);
  readonly columnas = ['dispositivo', 'sistema', 'version', 'actividad', 'estado', 'acciones'];

  constructor() {
    this.cargar();
  }

  icono(tipo: string): string {
    return ICONOS_DISPOSITIVO[tipo] ?? 'devices';
  }

  private cargar(): void {
    const id = this.auth.getIdUsuario();
    if (id === null) return;
    this.cargando.set(true);
    this.dispositivoService.porUsuario(id).subscribe({
      next: (lista) => {
        this.dispositivos.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  eliminar(d: Dispositivo): void {
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Eliminar dispositivo?',
          mensaje: `Se quitará "${d.modelo ?? d.tipoDispositivo}" de tu cuenta.`,
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.dispositivoService.eliminar(d.idDispositivo as number).subscribe({
          next: () => {
            this.notificacion.exito('Dispositivo eliminado');
            this.cargar();
          },
          error: (err) => this.notificacion.error(err),
        });
      });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
