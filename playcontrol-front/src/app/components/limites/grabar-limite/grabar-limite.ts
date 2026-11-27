import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ESTADOS_ACTIVO, EstadoActivo } from '../../../models/dispositivo';
import { DIAS_SEMANA } from '../../../models/jugador-joven';
import {
  ACCIONES_AL_EXCEDER,
  AccionAlExceder,
  LimiteJuego,
  METODOS_BLOQUEO,
  MetodoBloqueo,
} from '../../../models/limite-juego';
import { AuthService } from '../../../services/auth-service';
import { LimiteService } from '../../../services/limite-service';
import { NotificacionService } from '../../../services/notificacion-service';

// Formulario único para crear y editar un límite (editar si la ruta trae :id).
// ?usuario=<idUsuario> permite crear el límite de otro usuario (lo usa el padre con el idUsuarioHijo).
// Al guardar uno ACTIVO el backend pasa los demás a INACTIVO. idCreadoPor lo asigna el servidor.
@Component({
  selector: 'app-grabar-limite',
  standalone: false,
  templateUrl: './grabar-limite.html',
  styleUrl: './grabar-limite.css',
})
export class GrabarLimite {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private limiteService = inject(LimiteService);
  private auth = inject(AuthService);
  private notificacion = inject(NotificacionService);

  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  readonly editando = this.id !== null;
  private idUsuarioDestino =
    Number(this.route.snapshot.queryParamMap.get('usuario')) ||
    (this.auth.getIdUsuario() as number);

  readonly cargando = signal(this.editando);
  readonly guardando = signal(false);
  private original: LimiteJuego | null = null;

  // Valores permitidos (sección 5 del contrato)
  readonly diasSemana = DIAS_SEMANA;
  readonly metodos = METODOS_BLOQUEO;
  readonly acciones = ACCIONES_AL_EXCEDER;
  readonly estados = ESTADOS_ACTIVO;

  readonly form = this.fb.nonNullable.group({
    limiteHorasDiarias: [2, [Validators.required, Validators.min(0.5), Validators.max(24)]],
    limiteHorasSemanal: [14, [Validators.required, Validators.min(0.5), Validators.max(168)]],
    limiteHorasMensual: [60, [Validators.required, Validators.min(0.5), Validators.max(744)]],
    horaInicioDia: ['15:00', Validators.required],
    horaFinDia: ['21:00', Validators.required],
    diasPermitidos: [['LUN', 'MAR', 'MIE', 'JUE', 'VIE'] as string[], Validators.required],
    minutosAvisoPrevio: [15, [Validators.required, Validators.min(0), Validators.max(120)]],
    metodoBloqueo: ['NOTIFICACION' as MetodoBloqueo, Validators.required],
    accionAlExceder: ['ALERTAR' as AccionAlExceder, Validators.required],
    estado: ['ACTIVO' as EstadoActivo, Validators.required],
    observaciones: [''],
  });

  constructor() {
    if (this.id !== null) {
      this.limiteService.obtener(this.id).subscribe({
        next: (limite) => {
          this.original = limite;
          this.idUsuarioDestino = limite.idUsuario;
          this.form.patchValue({
            limiteHorasDiarias: limite.limiteHorasDiarias ?? 2,
            limiteHorasSemanal: limite.limiteHorasSemanal ?? 14,
            limiteHorasMensual: limite.limiteHorasMensual ?? 60,
            horaInicioDia: limite.horaInicioDia?.slice(0, 5) ?? '15:00',
            horaFinDia: limite.horaFinDia?.slice(0, 5) ?? '21:00',
            diasPermitidos: limite.diasPermitidos
              ? limite.diasPermitidos.split(',').map((d) => d.trim())
              : [],
            minutosAvisoPrevio: limite.minutosAvisoPrevio ?? 15,
            metodoBloqueo: limite.metodoBloqueo ?? 'NOTIFICACION',
            accionAlExceder: limite.accionAlExceder ?? 'ALERTAR',
            estado: limite.estado ?? 'ACTIVO',
            observaciones: limite.observaciones ?? '',
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
    // Se fusiona con el original porque PUT reemplaza el registro completo (sin idCreadoPor)
    const limite: LimiteJuego = {
      ...this.original,
      idCreadoPor: undefined, // lo fija el servidor
      idUsuario: this.idUsuarioDestino,
      limiteHorasDiarias: v.limiteHorasDiarias,
      limiteHorasSemanal: v.limiteHorasSemanal,
      limiteHorasMensual: v.limiteHorasMensual,
      horaInicioDia: `${v.horaInicioDia}:00`,
      horaFinDia: `${v.horaFinDia}:00`,
      // En el orden de la semana y sin espacios: "LUN,MAR,MIE"
      diasPermitidos: DIAS_SEMANA.map((d) => d.valor)
        .filter((d) => v.diasPermitidos.includes(d))
        .join(','),
      minutosAvisoPrevio: v.minutosAvisoPrevio,
      metodoBloqueo: v.metodoBloqueo,
      accionAlExceder: v.accionAlExceder,
      estado: v.estado,
      observaciones: v.observaciones,
    };

    this.guardando.set(true);
    const peticion =
      this.id !== null
        ? this.limiteService.actualizar(this.id, limite)
        : this.limiteService.crear(limite);
    peticion.subscribe({
      next: () => {
        this.notificacion.exito(this.editando ? 'Límite actualizado' : 'Límite creado');
        this.volver();
      },
      error: (err) => {
        this.guardando.set(false);
        this.notificacion.error(err, 'No se pudo guardar el límite', this.form);
      },
    });
  }

  /** Vuelve a ?volver= (p. ej. el detalle del hijo) o a la pantalla por defecto del rol */
  volver(): void {
    const destino = this.route.snapshot.queryParamMap.get('volver');
    // Solo rutas internas, para evitar redirecciones abiertas
    if (destino?.startsWith('/') && !destino.startsWith('//')) {
      this.router.navigateByUrl(destino);
      return;
    }
    this.router.navigate([this.auth.tieneRol('ROLE_PADRE') ? '/familia' : '/limites']);
  }
}
