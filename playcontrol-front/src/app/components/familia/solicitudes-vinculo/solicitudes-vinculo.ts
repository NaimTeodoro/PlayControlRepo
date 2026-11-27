import { Component, computed, inject, signal } from '@angular/core';
import { PadreHijo, TIPOS_RELACION } from '../../../models/padre-hijo';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { PadreHijoService } from '../../../services/padre-hijo-service';

// Solicitudes de vínculo del jugador: GET /padre-hijo/joven/{idJugadorJoven}.
// Las PENDIENTE se autorizan con PUT /padre-hijo/{id}/autorizar (solo el jugador).
@Component({
  selector: 'app-solicitudes-vinculo',
  standalone: false,
  templateUrl: './solicitudes-vinculo.html',
  styleUrl: './solicitudes-vinculo.css',
})
export class SolicitudesVinculo {
  private auth = inject(AuthService);
  private padreHijoService = inject(PadreHijoService);
  private notificacion = inject(NotificacionService);

  private idJugadorJoven = this.auth.getIdJugadorJoven();
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly sinPerfil = signal(this.idJugadorJoven === null);
  readonly autorizando = signal<number | null>(null);
  private vinculos = signal<PadreHijo[]>([]);

  readonly pendientes = computed(() => this.vinculos().filter((v) => v.estado === 'PENDIENTE'));
  readonly activos = computed(() => this.vinculos().filter((v) => v.estado === 'ACTIVO'));

  constructor() {
    if (this.idJugadorJoven === null) {
      this.cargando.set(false);
      return;
    }
    this.padreHijoService.porJoven(this.idJugadorJoven).subscribe({
      next: (lista) => {
        this.vinculos.set(lista);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  relacion(tipo?: string | null): string {
    return TIPOS_RELACION.find((t) => t.valor === tipo)?.etiqueta ?? 'Familiar';
  }

  /** PUT /padre-hijo/{id}/autorizar → el vínculo pasa a ACTIVO */
  autorizar(v: PadreHijo): void {
    const id = v.idPadreHijo as number;
    this.autorizando.set(id);
    this.padreHijoService.autorizar(id).subscribe({
      next: (actualizado) => {
        this.vinculos.update((l) => l.map((x) => (x.idPadreHijo === id ? actualizado : x)));
        this.autorizando.set(null);
        this.notificacion.exito('Solicitud autorizada');
      },
      error: (err) => {
        this.autorizando.set(null);
        this.notificacion.error(err, 'No se pudo autorizar la solicitud');
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
