import { Component, computed, inject, signal } from '@angular/core';
import { catchError, forkJoin, of } from 'rxjs';
import { AlertaJuego } from '../../../models/alerta-juego';
import { Cita } from '../../../models/cita';
import { Puntos } from '../../../models/puntos';
import { Recompensa } from '../../../models/recompensa';
import { ResumenSesiones } from '../../../models/resumen-sesiones';
import {
  AlertaService,
  claseIntensidad,
  infoTipoAlerta,
  ordenarAlertas,
} from '../../../services/alerta-service';
import { AuthService } from '../../../services/auth-service';
import {
  CitaService,
  etiquetaFaltan,
  fechaCita,
  nombreProfesional,
  proximaCita,
} from '../../../services/cita-service';
import { JugadorJovenService } from '../../../services/jugador-joven-service';
import { PadreHijoService } from '../../../services/padre-hijo-service';
import { claseProgreso, LimiteService } from '../../../services/limite-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { RecompensaService } from '../../../services/recompensa-service';
import { formatoDuracion, SesionService } from '../../../services/sesion-service';

// Inicio del jugador: tiempo de hoy vs. límite (resumen del backend), alertas pendientes,
// puntos y logros en progreso
@Component({
  selector: 'app-inicio-jugador',
  standalone: false,
  templateUrl: './inicio-jugador.html',
  styleUrl: './inicio-jugador.css',
})
export class InicioJugador {
  private auth = inject(AuthService);
  private notificacion = inject(NotificacionService);

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly codigoVinculacion = signal<string | null>(null);
  readonly solicitudesPendientes = signal(0);
  /** El padre/tutor controla el límite del jugador */
  readonly limiteControlado = signal(false);

  readonly resumen = signal<ResumenSesiones | null>(null);
  readonly puntos = signal<Puntos | null>(null);
  private alertas = signal<AlertaJuego[]>([]);
  private recompensas = signal<Recompensa[]>([]);
  private citas = signal<Cita[]>([]);

  readonly nombreProfesional = nombreProfesional;
  readonly etiquetaFaltan = etiquetaFaltan;
  readonly fechaCita = fechaCita;
  /** Próxima consulta con un profesional (si no hay, no se muestra la tarjeta) */
  readonly proximaCita = computed(() => proximaCita(this.citas()));

  readonly formatoDuracion = formatoDuracion;
  readonly claseProgreso = claseProgreso;
  readonly infoTipo = infoTipoAlerta;
  readonly claseIntensidad = claseIntensidad;

  readonly minutosHoy = computed(() => this.resumen()?.minutosHoy ?? 0);
  readonly limiteMinutos = computed(() => this.resumen()?.limiteDiarioMinutos ?? null);
  readonly porcentaje = computed(() => this.resumen()?.porcentajeHoy ?? null);
  readonly restante = computed(() => {
    const limite = this.limiteMinutos();
    return limite !== null ? Math.max(0, limite - this.minutosHoy()) : null;
  });
  readonly enCurso = computed(() => this.resumen()?.sesionEnCurso ?? null);

  readonly pendientes = computed(() => ordenarAlertas(this.alertas()));

  // Logros en progreso con más avance primero
  readonly enProgreso = computed(() =>
    this.recompensas()
      .filter((r) => r.estado === 'EN_PROGRESO')
      .sort((a, b) => (b.porcentajeProgreso ?? 0) - (a.porcentajeProgreso ?? 0))
      .slice(0, 4),
  );
  readonly porReclamar = computed(
    () => this.recompensas().filter((r) => r.estado === 'OBTENIDA').length,
  );

  constructor() {
    const id = this.auth.getIdUsuario();
    if (id === null) return;
    const recompensaService = inject(RecompensaService);

    forkJoin({
      resumen: inject(SesionService).resumen(id),
      alertas: inject(AlertaService).pendientes(id),
      puntos: recompensaService.puntos(id),
      recompensas: recompensaService.porUsuario(id),
    }).subscribe({
      next: (d) => {
        this.resumen.set(d.resumen);
        this.alertas.set(d.alertas);
        this.puntos.set(d.puntos);
        this.recompensas.set(d.recompensas);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });

    // La próxima cita es un extra: si falla, el inicio carga igual
    inject(CitaService)
      .mias()
      .pipe(catchError(() => of([] as Cita[])))
      .subscribe((c) => this.citas.set(c));

    inject(LimiteService)
      .estaControladoPorPadre()
      .subscribe((c) => this.limiteControlado.set(c.controlado));

    // Solicitudes de vínculo pendientes (aviso con enlace a /solicitudes)
    const idJoven = this.auth.getIdJugadorJoven();
    if (idJoven !== null) {
      inject(PadreHijoService)
        .porJoven(idJoven)
        .subscribe({
          next: (l) => this.solicitudesPendientes.set(l.filter((v) => v.estado === 'PENDIENTE').length),
          error: () => this.solicitudesPendientes.set(0),
        });
    }

    // Código de vinculación que el padre necesita para vincularse (si no hay perfil, no se muestra)
    inject(JugadorJovenService)
      .porUsuario(id)
      .subscribe({
        next: (perfil) => this.codigoVinculacion.set(perfil.codigoVinculacion ?? null),
        error: () => this.codigoVinculacion.set(null),
      });
  }

  copiarCodigo(): void {
    const codigo = this.codigoVinculacion();
    if (!codigo) return;
    navigator.clipboard
      ?.writeText(codigo)
      .then(() => this.notificacion.exito('Código copiado'))
      .catch(() => this.notificacion.error('No se pudo copiar el código'));
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
