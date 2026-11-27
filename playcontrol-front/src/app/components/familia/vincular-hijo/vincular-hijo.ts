import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { TipoRelacion, TIPOS_RELACION } from '../../../models/padre-hijo';
import { AuthService } from '../../../services/auth-service';
import { mensajeError, NotificacionService } from '../../../services/notificacion-service';
import { PadreHijoService } from '../../../services/padre-hijo-service';

// Vincular un hijo: el padre ingresa el código de vinculación del hijo (PC-XXXXXX) y
// POST /padre-hijo/vincular crea el vínculo PENDIENTE hasta que el hijo lo autorice.
@Component({
  selector: 'app-vincular-hijo',
  standalone: false,
  templateUrl: './vincular-hijo.html',
  styleUrl: './vincular-hijo.css',
})
export class VincularHijo {
  private router = inject(Router);
  private padreHijoService = inject(PadreHijoService);
  private notificacion = inject(NotificacionService);

  readonly idPadreTutor = inject(AuthService).getIdPadreTutor();
  readonly guardando = signal(false);
  readonly relaciones = TIPOS_RELACION;

  readonly form = inject(FormBuilder).nonNullable.group({
    // PC- seguido de 6 letras o números (el backend normaliza espacios y mayúsculas)
    codigoVinculacion: [
      '',
      [Validators.required, Validators.pattern(/^\s*[Pp][Cc]-[A-Za-z0-9]{6}\s*$/)],
    ],
    tipoRelacion: ['PADRE' as TipoRelacion, Validators.required],
  });

  vincular(): void {
    if (this.form.invalid || this.idPadreTutor === null) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    this.guardando.set(true);
    this.padreHijoService
      .vincular({
        idPadreTutor: this.idPadreTutor,
        codigoVinculacion: v.codigoVinculacion.trim().toUpperCase(),
        tipoRelacion: v.tipoRelacion,
      })
      .subscribe({
        next: (vinculo) => {
          const nombre = vinculo.nombreHijo ?? 'tu hijo';
          this.notificacion.exito(`Solicitud enviada. Falta que ${nombre} la autorice.`);
          this.router.navigate(['/familia']);
        },
        error: (err) => {
          this.guardando.set(false);
          if (err instanceof HttpErrorResponse && err.status === 404) {
            this.form.controls.codigoVinculacion.setErrors({ inexistente: true });
            this.notificacion.error('No existe ningún jugador con ese código');
          } else if (err instanceof HttpErrorResponse && err.status === 409) {
            // 409: ya existe el vínculo (PENDIENTE o ACTIVO) con ese jugador
            const detalle = mensajeError(err, 'Ya estás vinculado con este jugador');
            this.form.controls.codigoVinculacion.setErrors({ servidor: detalle });
            this.notificacion.error(err, 'Ya estás vinculado con este jugador');
          } else {
            this.notificacion.error(err, 'No se pudo vincular', this.form);
          }
        },
      });
  }
}
