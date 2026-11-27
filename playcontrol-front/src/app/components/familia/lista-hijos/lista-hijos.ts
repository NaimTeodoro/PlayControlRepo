import { Component, inject, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PadreHijo, TIPOS_RELACION } from '../../../models/padre-hijo';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { nombreHijo, PadreHijoService, relacionActiva } from '../../../services/padre-hijo-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// "Familia": hijos vinculados (GET /padre-hijo/padre/{idPadreTutor}, ya trae nombre y código del hijo)
@Component({
  selector: 'app-lista-hijos',
  standalone: false,
  templateUrl: './lista-hijos.html',
  styleUrl: './lista-hijos.css',
})
export class ListaHijos {
  private auth = inject(AuthService);
  private padreHijoService = inject(PadreHijoService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  private idPadreTutor = this.auth.getIdPadreTutor();
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly sinPerfil = signal(this.idPadreTutor === null);
  readonly hijos = signal<PadreHijo[]>([]);
  readonly columnas = ['hijo', 'relacion', 'estado', 'permisos', 'fecha', 'acciones'];
  readonly relacionActiva = relacionActiva;
  readonly nombreHijo = nombreHijo;

  constructor() {
    this.cargar();
  }

  /** "VER_REPORTES,CONFIGURAR_LIMITES" → ["Ver reportes", "Configurar limites"] */
  permisos(texto?: string | null): string[] {
    return (texto ?? '')
      .split(',')
      .map((p) => p.trim().toLowerCase().replace(/_/g, ' '))
      .filter((p) => p.length > 0)
      .map((p) => p.charAt(0).toUpperCase() + p.slice(1));
  }

  relacion(tipo?: string | null): string {
    return TIPOS_RELACION.find((t) => t.valor === tipo)?.etiqueta ?? '—';
  }

  private cargar(): void {
    if (this.idPadreTutor === null) {
      this.cargando.set(false);
      return;
    }
    this.cargando.set(true);
    this.padreHijoService.porPadre(this.idPadreTutor).subscribe({
      next: (hijos) => {
        // Activos primero
        this.hijos.set(
          [...hijos].sort((a, b) => Number(relacionActiva(b)) - Number(relacionActiva(a))),
        );
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  desvincular(hijo: PadreHijo): void {
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Desvincular a este hijo?',
          mensaje: `Dejarás de ver la actividad de ${nombreHijo(hijo)}. Podrás volver a vincularlo con su código.`,
          textoConfirmar: 'Desvincular',
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.padreHijoService.eliminar(hijo.idPadreHijo as number).subscribe({
          next: () => {
            this.notificacion.exito('Hijo desvinculado');
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
