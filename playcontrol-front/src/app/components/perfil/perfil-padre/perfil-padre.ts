import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { catchError, forkJoin, of } from 'rxjs';
import { PadreHijo } from '../../../models/padre-hijo';
import {
  FRECUENCIAS_REVISION,
  FrecuenciaRevision,
  PadreTutor,
} from '../../../models/padre-tutor';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { PadreHijoService, relacionActiva } from '../../../services/padre-hijo-service';
import { PadreTutorService } from '../../../services/padre-tutor-service';

/** Claves conocidas de configuracionAlertas (JSON en texto) */
interface ConfigAlertas {
  limiteDiario?: boolean;
  horario?: boolean;
  [clave: string]: unknown;
}

/** Lee el JSON de configuracionAlertas sin romper si viene vacío o mal formado */
function leerConfig(texto?: string | null): ConfigAlertas {
  try {
    const valor = texto ? JSON.parse(texto) : {};
    return valor && typeof valor === 'object' ? valor : {};
  } catch {
    return {};
  }
}

// Perfil del padre/tutor: PadreTutorDTO (GET /padres-tutores/usuario/{idUsuario}) editable con
// PUT /padres-tutores/{idPadreTutor}, más un resumen de sus vínculos.
@Component({
  selector: 'app-perfil-padre',
  standalone: false,
  templateUrl: './perfil-padre.html',
  styleUrl: './perfil-padre.css',
})
export class PerfilPadre {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private padreService = inject(PadreTutorService);
  private padreHijoService = inject(PadreHijoService);
  private notificacion = inject(NotificacionService);

  readonly nombre = this.auth.getNombreCompleto();
  readonly username = this.auth.getUsername();
  readonly inicial = (this.nombre || '?').charAt(0).toUpperCase();
  readonly frecuencias = FRECUENCIAS_REVISION;

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly guardando = signal(false);
  readonly editando = signal(false);
  readonly perfil = signal<PadreTutor | null>(null);
  readonly vinculos = signal<PadreHijo[]>([]);

  readonly activos = computed(() => this.vinculos().filter(relacionActiva).length);
  readonly pendientes = computed(
    () => this.vinculos().filter((v) => v.estado === 'PENDIENTE').length,
  );
  readonly config = computed(() => leerConfig(this.perfil()?.configuracionAlertas));

  readonly form = this.fb.nonNullable.group({
    metodosControlActual: ['', Validators.maxLength(255)],
    preocupacionesPrincipales: ['', Validators.maxLength(255)],
    objetivosSupervision: ['', Validators.maxLength(255)],
    frecuenciaRevisionReportes: ['SEMANAL' as FrecuenciaRevision, Validators.required],
    alertaLimiteDiario: [true],
    alertaHorario: [true],
  });

  constructor() {
    const idUsuario = this.auth.getIdUsuario() as number;
    const idPadreTutor = this.auth.getIdPadreTutor();
    forkJoin({
      perfil: this.padreService.porUsuario(idUsuario),
      // El resumen de hijos es opcional: si falla, el perfil se muestra igual
      vinculos:
        idPadreTutor !== null
          ? this.padreHijoService.porPadre(idPadreTutor).pipe(catchError(() => of([])))
          : of([] as PadreHijo[]),
    }).subscribe({
      next: ({ perfil, vinculos }) => {
        this.perfil.set(perfil);
        this.vinculos.set(vinculos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  etiquetaFrecuencia(valor?: string | null): string {
    return FRECUENCIAS_REVISION.find((f) => f.valor === valor)?.etiqueta ?? '—';
  }

  editar(): void {
    const p = this.perfil();
    if (!p) return;
    const config = this.config();
    this.form.patchValue({
      metodosControlActual: p.metodosControlActual ?? '',
      preocupacionesPrincipales: p.preocupacionesPrincipales ?? '',
      objetivosSupervision: p.objetivosSupervision ?? '',
      frecuenciaRevisionReportes: p.frecuenciaRevisionReportes ?? 'SEMANAL',
      alertaLimiteDiario: config.limiteDiario !== false,
      alertaHorario: config.horario !== false,
    });
    this.editando.set(true);
  }

  cancelar(): void {
    this.editando.set(false);
  }

  guardar(): void {
    const original = this.perfil();
    if (this.form.invalid || !original?.idPadreTutor) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    // Se conservan las demás claves del JSON de alertas
    const config: ConfigAlertas = {
      ...this.config(),
      limiteDiario: v.alertaLimiteDiario,
      horario: v.alertaHorario,
    };
    // Se fusiona con el original porque PUT reemplaza el registro completo
    const datos: PadreTutor = {
      ...original,
      metodosControlActual: v.metodosControlActual.trim() || null,
      preocupacionesPrincipales: v.preocupacionesPrincipales.trim() || null,
      objetivosSupervision: v.objetivosSupervision.trim() || null,
      frecuenciaRevisionReportes: v.frecuenciaRevisionReportes,
      configuracionAlertas: JSON.stringify(config),
    };

    this.guardando.set(true);
    this.padreService.actualizar(original.idPadreTutor, datos).subscribe({
      next: (guardado) => {
        this.perfil.set(guardado);
        this.guardando.set(false);
        this.editando.set(false);
        this.notificacion.exito('Perfil actualizado');
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
