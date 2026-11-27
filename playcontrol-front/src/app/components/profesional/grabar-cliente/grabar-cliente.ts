import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ValidationErrors, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { SelectorHora } from '../../confirmaciones/selector-hora/selector-hora';
import { of, switchMap } from 'rxjs';
import { ProfesionalCliente } from '../../../models/profesional-cliente';
import { EstadoProfesional, ESTADOS_PROFESIONAL } from '../../../models/profesional-salud';
import { AuthService } from '../../../services/auth-service';
import { JugadorJovenService } from '../../../services/jugador-joven-service';
import { NotificacionService } from '../../../services/notificacion-service';
import {
  nombreCliente,
  ProfesionalClienteService,
} from '../../../services/profesional-cliente-service';

// Formulario único para crear y editar un cliente (editar si la ruta trae :id).
// Al crear, el jugador se elige por su código de vinculación (GET /jugadores-jovenes/codigo/{codigo}).
// fechaAsociacion la pone el servidor; 409 si el jugador ya es cliente del profesional.
@Component({
  selector: 'app-grabar-cliente',
  standalone: false,
  templateUrl: './grabar-cliente.html',
  styleUrl: './grabar-cliente.css',
})
export class GrabarCliente {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private clienteService = inject(ProfesionalClienteService);
  private jugadorJovenService = inject(JugadorJovenService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly editando = this.id !== null;
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly guardando = signal(false);
  private idProfesional = inject(AuthService).getIdProfesional();
  readonly sinPerfil = signal(this.idProfesional === null);
  readonly nombreJugador = signal('');

  private original: ProfesionalCliente | null = null;

  readonly frecuencias = ['Semanal', 'Quincenal', 'Mensual'];
  readonly estados = ESTADOS_PROFESIONAL;

  readonly form = this.fb.nonNullable.group({
    codigoVinculacion: [
      '',
      [Validators.required, Validators.pattern(/^\s*[Pp][Cc]-[A-Za-z0-9]{6}\s*$/)],
    ],
    estado: ['ACTIVO' as EstadoProfesional, Validators.required],
    frecuenciaConsulta: ['Semanal'],
    proximaFecha: [null as Date | null], // datepicker
    proximaHora: [null as Date | null], // timepicker
    diagnostico: [''],
    planTratamiento: [''],
    notas: [''],
  }, { validators: [(g) => this.validarConsulta(g)] });

  readonly hoy = new Date();
  private consultaOriginal: string | null = null;

  /** La consulta requiere fecha y hora juntas, y no puede estar en el pasado (salvo que no se haya cambiado al editar) */
  private validarConsulta(g: AbstractControl): ValidationErrors | null {
    const iso = this.consultaIso(g.get('proximaFecha')?.value, g.get('proximaHora')?.value);
    const fecha = g.get('proximaFecha')?.value as Date | null;
    const hora = g.get('proximaHora')?.value as Date | null;
    if (!fecha && !hora) return null;
    if (!fecha || !hora) return { consultaIncompleta: true };
    if (iso && iso !== this.consultaOriginal && new Date(iso) < new Date()) {
      return { consultaPasada: true };
    }
    return null;
  }

  /** Une fecha y hora en ISO sin zona (yyyy-MM-ddTHH:mm:00) */
  private consultaIso(fecha: Date | null, hora: Date | null): string | null {
    if (!fecha || !hora) return null;
    const p = (n: number) => String(n).padStart(2, '0');
    return `${fecha.getFullYear()}-${p(fecha.getMonth() + 1)}-${p(fecha.getDate())}T${p(hora.getHours())}:${p(hora.getMinutes())}:00`;
  }

  constructor() {
    if (this.idProfesional === null || this.id === null) {
      this.cargando.set(false);
      return;
    }
    // Editar: solo un cliente propio
    this.clienteService.porProfesional(this.idProfesional).subscribe({
      next: (clientes) => {
        this.cargando.set(false);
        const cliente = clientes.find((c) => c.idProfesionalCliente === this.id);
        if (!cliente) {
          this.notificacion.error('No se encontró el cliente');
          this.router.navigate(['/clientes']);
          return;
        }
        this.original = cliente;
        this.consultaOriginal = cliente.proximaConsulta?.slice(0, 19) ?? null;
        this.nombreJugador.set(nombreCliente(cliente));
        this.form.patchValue({
          estado: cliente.estado ?? 'ACTIVO',
          frecuenciaConsulta: cliente.frecuenciaConsulta ?? '',
          proximaFecha: cliente.proximaConsulta ? new Date(cliente.proximaConsulta.slice(0, 19)) : null,
          proximaHora: cliente.proximaConsulta ? new Date(cliente.proximaConsulta.slice(0, 19)) : null,
          diagnostico: cliente.diagnostico ?? '',
          planTratamiento: cliente.planTratamiento ?? '',
          notas: cliente.notas ?? '',
        });
        // El jugador no se cambia al editar
        this.form.controls.codigoVinculacion.disable();
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  /** Hora elegida como texto HH:mm */
  textoHora(): string {
    const h = this.form.controls.proximaHora.value;
    return h ? `${String(h.getHours()).padStart(2, '0')}:${String(h.getMinutes()).padStart(2, '0')}` : '';
  }

  /** Abre el reloj para elegir la hora de la consulta */
  elegirHora(): void {
    this.dialog
      .open(SelectorHora, { data: { hora: this.form.controls.proximaHora.value } })
      .afterClosed()
      .subscribe((hora?: Date) => {
        if (!hora) return;
        this.form.controls.proximaHora.setValue(hora);
        this.form.controls.proximaHora.markAsDirty();
      });
  }

  guardar(): void {
    if (this.form.invalid || this.idProfesional === null) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const idProfesional = this.idProfesional;
    const datos = {
      estado: v.estado,
      frecuenciaConsulta: v.frecuenciaConsulta,
      proximaConsulta: this.consultaIso(v.proximaFecha, v.proximaHora),
      diagnostico: v.diagnostico,
      planTratamiento: v.planTratamiento,
      notas: v.notas,
    };

    this.guardando.set(true);
    const peticion =
      this.id !== null && this.original
        ? // PUT: se fusiona con el original (reemplaza el registro completo)
          this.clienteService.actualizar(this.id, { ...this.original, ...datos })
        : // POST: primero se resuelve el código de vinculación → idJugadorJoven
          this.jugadorJovenService.porCodigo(v.codigoVinculacion.trim().toUpperCase()).pipe(
            switchMap((jugador) =>
              jugador.idJugadorJoven
                ? this.clienteService.crear({
                    idProfesional,
                    idJugadorJoven: jugador.idJugadorJoven,
                    ...datos,
                  } as ProfesionalCliente)
                : of(null),
            ),
          );
    peticion.subscribe({
      next: () => {
        this.notificacion.exito(this.editando ? 'Cliente actualizado' : 'Cliente registrado');
        this.router.navigate(['/clientes']);
      },
      error: (err) => {
        this.guardando.set(false);
        if (err instanceof HttpErrorResponse && err.status === 404 && this.id === null) {
          this.form.controls.codigoVinculacion.setErrors({ inexistente: true });
          this.notificacion.error('No existe ningún jugador con ese código');
        } else {
          // 409: el jugador ya es tu cliente
          this.notificacion.error(err, 'No se pudo guardar el cliente', this.form);
        }
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
