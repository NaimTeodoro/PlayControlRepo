import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AlertaJuego } from '../../../models/alerta-juego';
import {
  AlertaService,
  claseIntensidad,
  esPendiente,
  infoTipoAlerta,
} from '../../../services/alerta-service';
import { HistorialAlerta } from '../../../models/historial-alerta';
import { HistorialAlertaService } from '../../../services/historial-alerta-service';
import { NotificacionService } from '../../../services/notificacion-service';

// Detalle de una alerta; si está pendiente se responde con PUT /alertas-juego/{id}/responder
@Component({
  selector: 'app-detalle-alerta',
  standalone: false,
  templateUrl: './detalle-alerta.html',
  styleUrl: './detalle-alerta.css',
})
export class DetalleAlerta {
  private alertaService = inject(AlertaService);
  private notificacion = inject(NotificacionService);

  private historialService = inject(HistorialAlertaService);
  readonly historial = signal<HistorialAlerta[]>([]);
  readonly alerta = signal<AlertaJuego | null>(null);
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly respondiendo = signal(false);

  readonly form = inject(FormBuilder).nonNullable.group({
    respuestaUsuario: [
      'Entendido, voy a hacer una pausa',
      [Validators.required, Validators.maxLength(500)],
    ],
  });

  readonly esPendiente = esPendiente;
  readonly infoTipo = infoTipoAlerta;
  readonly claseIntensidad = claseIntensidad;

  constructor() {
    const id = Number(inject(ActivatedRoute).snapshot.paramMap.get('id'));
    this.alertaService.obtener(id).subscribe({
      next: (alerta) => {
        this.alerta.set(alerta);
        this.cargando.set(false);
        this.cargarHistorial(id);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err, 'No se encontró la alerta');
      },
    });
  }

  /** Historial de la alerta; si falla, simplemente no se muestra */
  private cargarHistorial(id: number): void {
    this.historialService.porAlerta(id).subscribe({
      next: (h) => this.historial.set(h),
      error: () => this.historial.set([]),
    });
  }

  responder(alerta: AlertaJuego): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.respondiendo.set(true);
    const respuesta = this.form.getRawValue().respuestaUsuario.trim();
    this.alertaService.responder(alerta.idAlerta as number, respuesta).subscribe({
      next: (actualizada) => {
        this.alerta.set(actualizada);
        this.cargarHistorial(alerta.idAlerta as number);
        this.respondiendo.set(false);
        this.notificacion.exito('Alerta marcada como respondida');
      },
      error: (err) => {
        this.respondiendo.set(false);
        // 409: ya estaba respondida
        this.notificacion.error(err, 'No se pudo responder la alerta', this.form);
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
