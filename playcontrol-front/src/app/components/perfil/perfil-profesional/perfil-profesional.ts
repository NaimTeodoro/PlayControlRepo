import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ESTADOS_PROFESIONAL, perfilIncompleto, ProfesionalSalud } from '../../../models/profesional-salud';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { ProfesionalService } from '../../../services/profesional-service';

// Perfil profesional (ProfesionalSalud). El backend lo crea al registrarse (estado PENDIENTE);
// aquí se ve y se completa/edita con PUT /profesionales-salud/{id}
@Component({
  selector: 'app-perfil-profesional',
  standalone: false,
  templateUrl: './perfil-profesional.html',
  styleUrl: './perfil-profesional.css',
})
export class PerfilProfesional {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private profesionalService = inject(ProfesionalService);
  private notificacion = inject(NotificacionService);

  readonly inicial = (this.auth.getUsername() ?? '?').charAt(0).toUpperCase();
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly guardando = signal(false);
  readonly editando = signal(false);
  readonly perfil = signal<ProfesionalSalud | null>(null);
  readonly incompleto = signal(false);

  readonly form = this.fb.nonNullable.group({
    especialidad: ['', [Validators.required, Validators.maxLength(150)]],
    numeroLicencia: ['', [Validators.required, Validators.maxLength(100)]],
    institucion: ['', Validators.maxLength(200)],
    aniosExperiencia: [null as number | null, [Validators.min(0), Validators.max(70)]],
    areaTrabajo: [''],
    certificaciones: [''],
    telefonoConsulta: ['', Validators.maxLength(20)],
    horarioConsulta: ['', Validators.maxLength(200)],
  });

  constructor() {
    this.profesionalService.porUsuario(this.auth.getIdUsuario() as number).subscribe({
      next: (perfil) => {
        this.perfil.set(perfil);
        this.cargarForm(perfil);
        // Perfil recién creado (sin especialidad/licencia): se abre el formulario directamente
        const incompleto = perfilIncompleto(perfil);
        this.incompleto.set(incompleto);
        this.editando.set(incompleto);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  private cargarForm(p: ProfesionalSalud): void {
    this.form.patchValue({
      especialidad: p.especialidad === 'Por completar' ? '' : (p.especialidad ?? ''),
      numeroLicencia: p.numeroLicencia?.startsWith('PENDIENTE-') ? '' : (p.numeroLicencia ?? ''),
      institucion: p.institucion ?? '',
      aniosExperiencia: p.aniosExperiencia ?? null,
      areaTrabajo: p.areaTrabajo ?? '',
      certificaciones: p.certificaciones ?? '',
      telefonoConsulta: p.telefonoConsulta ?? '',
      horarioConsulta: p.horarioConsulta ?? '',
    });
  }

  editar(): void {
    const p = this.perfil();
    if (p) this.cargarForm(p);
    this.editando.set(true);
  }

  estado(valor?: string): string {
    return ESTADOS_PROFESIONAL.find((e) => e.valor === valor)?.etiqueta ?? '—';
  }

  cancelar(): void {
    this.editando.set(false);
  }

  guardar(): void {
    const original = this.perfil();
    if (this.form.invalid || !original?.idProfesional) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    // Se fusiona con el original (verificación, estado...) porque PUT reemplaza el registro completo
    const datos: ProfesionalSalud = {
      ...original,
      idUsuario: this.auth.getIdUsuario() as number,
      especialidad: v.especialidad.trim(),
      numeroLicencia: v.numeroLicencia.trim(),
      institucion: v.institucion,
      aniosExperiencia: v.aniosExperiencia ?? undefined,
      areaTrabajo: v.areaTrabajo,
      certificaciones: v.certificaciones,
      telefonoConsulta: v.telefonoConsulta,
      horarioConsulta: v.horarioConsulta,
    };

    this.guardando.set(true);
    this.profesionalService.actualizar(original.idProfesional, datos).subscribe({
      next: (guardado) => {
        this.perfil.set(guardado);
        this.guardando.set(false);
        this.editando.set(false);
        this.notificacion.exito(this.incompleto() ? '¡Perfil completado!' : 'Perfil actualizado');
        this.incompleto.set(false);
      },
      error: (err) => {
        this.guardando.set(false);
        this.notificacion.error(err, 'No se pudo guardar el perfil', this.form);
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
