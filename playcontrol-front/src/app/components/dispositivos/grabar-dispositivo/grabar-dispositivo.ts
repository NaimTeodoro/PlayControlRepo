import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  Dispositivo,
  EstadoActivo,
  ESTADOS_ACTIVO,
  TipoDispositivo,
  TIPOS_DISPOSITIVO,
} from '../../../models/dispositivo';
import { AuthService } from '../../../services/auth-service';
import { DispositivoService } from '../../../services/dispositivo-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { isoLocal } from '../../../services/sesion-service';

// Formulario único para crear y editar un dispositivo (editar si la ruta trae :id)
@Component({
  selector: 'app-grabar-dispositivo',
  standalone: false,
  templateUrl: './grabar-dispositivo.html',
  styleUrl: './grabar-dispositivo.css',
})
export class GrabarDispositivo {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private dispositivoService = inject(DispositivoService);
  private auth = inject(AuthService);
  private notificacion = inject(NotificacionService);

  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly editando = this.id !== null;
  readonly cargando = signal(this.editando);
  readonly guardando = signal(false);
  private original: Dispositivo | null = null;

  readonly tipos = TIPOS_DISPOSITIVO;
  readonly estados = ESTADOS_ACTIVO;

  readonly form = this.fb.nonNullable.group({
    tipoDispositivo: ['PC' as TipoDispositivo, Validators.required],
    modelo: ['', [Validators.required, Validators.maxLength(150)]],
    sistemaOperativo: ['', Validators.maxLength(100)],
    identificadorDisp: ['', Validators.maxLength(500)],
    versionApp: ['', Validators.maxLength(20)],
    estado: ['ACTIVO' as EstadoActivo, Validators.required],
  });

  constructor() {
    if (this.id !== null) {
      this.dispositivoService.obtener(this.id).subscribe({
        next: (d) => {
          this.original = d;
          this.form.patchValue({
            tipoDispositivo: d.tipoDispositivo,
            modelo: d.modelo ?? '',
            sistemaOperativo: d.sistemaOperativo ?? '',
            identificadorDisp: d.identificadorDisp ?? '',
            versionApp: d.versionApp ?? '',
            estado: d.estado ?? 'ACTIVO',
          });
          this.cargando.set(false);
        },
        error: (err) => {
          this.cargando.set(false);
          this.notificacion.error(err);
          this.volver();
        },
      });
    }
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const ahora = isoLocal();
    // Se fusiona con el original porque PUT reemplaza el registro completo
    const dispositivo: Dispositivo = {
      ...this.original,
      idUsuario: this.original?.idUsuario ?? (this.auth.getIdUsuario() as number),
      tipoDispositivo: v.tipoDispositivo,
      modelo: v.modelo.trim(),
      sistemaOperativo: v.sistemaOperativo.trim(),
      // Si no se indica un identificador, se genera uno
      identificadorDisp:
        v.identificadorDisp.trim() ||
        this.original?.identificadorDisp ||
        `DEV-${Date.now()}-${this.auth.getUsername()}`,
      versionApp: v.versionApp.trim(),
      estado: v.estado,
      fechaAsociacion: this.original?.fechaAsociacion ?? ahora,
      ultimaActividad: this.original?.ultimaActividad ?? ahora,
    };

    this.guardando.set(true);
    const peticion =
      this.id !== null
        ? this.dispositivoService.actualizar(this.id, dispositivo)
        : this.dispositivoService.crear(dispositivo);
    peticion.subscribe({
      next: () => {
        this.notificacion.exito(this.editando ? 'Dispositivo actualizado' : 'Dispositivo agregado');
        this.volver();
      },
      error: (err) => {
        this.guardando.set(false);
        this.notificacion.error(err, 'No se pudo guardar el dispositivo', this.form);
      },
    });
  }

  volver(): void {
    this.router.navigate(['/dispositivos']);
  }
}
