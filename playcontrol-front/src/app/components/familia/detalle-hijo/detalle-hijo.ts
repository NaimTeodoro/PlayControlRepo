import { Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, forkJoin, of, switchMap } from 'rxjs';
import { AlertaJuego } from '../../../models/alerta-juego';
import { Cita } from '../../../models/cita';
import { ACCIONES_AL_EXCEDER, LimiteJuego, METODOS_BLOQUEO } from '../../../models/limite-juego';
import { PadreHijo, TIPOS_RELACION } from '../../../models/padre-hijo';
import { Puntos } from '../../../models/puntos';
import { ESTADOS_RECOMPENSA, Recompensa } from '../../../models/recompensa';
import { PeriodoReporte } from '../../../models/reporte-padre';
import { ResumenSesiones } from '../../../models/resumen-sesiones';
import { SesionJuego } from '../../../models/sesion-juego';
import {
  AlertaService,
  claseIntensidad,
  esPendiente,
  infoTipoAlerta,
  ordenarAlertas,
} from '../../../services/alerta-service';
import { AuthService } from '../../../services/auth-service';
import { CitaService, proximaCita } from '../../../services/cita-service';
import { claseProgreso, LimiteService } from '../../../services/limite-service';
import { NotificacionService } from '../../../services/notificacion-service';
import {
  nombreHijo,
  PadreHijoService,
  puedeConfigurarLimites,
  relacionActiva,
} from '../../../services/padre-hijo-service';
import { RecompensaService } from '../../../services/recompensa-service';
import { ReportePadreService } from '../../../services/reporte-padre-service';
import { formatoDuracion, SesionService } from '../../../services/sesion-service';

// Orden de los logros: obtenidos/reclamados primero y luego por progreso
const ORDEN_LOGROS: Record<string, number> = { OBTENIDA: 0, RECLAMADA: 1, EN_PROGRESO: 2 };

// Detalle de un hijo (ruta /familia/hijo/:id con id = idPadreHijo). Los datos de juego se piden
// siempre con el idUsuarioHijo que trae el vínculo: resumen, sesiones, alertas, límites y puntos.
// El padre solo lee: no responde alertas ni reclama logros (eso lo hace el hijo).
@Component({
  selector: 'app-detalle-hijo',
  standalone: false,
  templateUrl: './detalle-hijo.html',
  styleUrl: './detalle-hijo.css',
})
export class DetalleHijo {
  private auth = inject(AuthService);
  private padreHijoService = inject(PadreHijoService);
  private sesionService = inject(SesionService);
  private limiteService = inject(LimiteService);
  private alertaService = inject(AlertaService);
  private recompensaService = inject(RecompensaService);
  private reporteService = inject(ReportePadreService);
  private citaService = inject(CitaService);
  private notificacion = inject(NotificacionService);
  private router = inject(Router);

  private idPadreHijo = Number(inject(ActivatedRoute).snapshot.paramMap.get('id'));

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly generando = signal(false);
  readonly hijo = signal<PadreHijo | null>(null);
  readonly resumen = signal<ResumenSesiones | null>(null);
  readonly sesiones = signal<SesionJuego[]>([]);
  readonly alertas = signal<AlertaJuego[]>([]);
  readonly limites = signal<LimiteJuego[]>([]);
  readonly recompensas = signal<Recompensa[]>([]);
  readonly citas = signal<Cita[]>([]);
  readonly citaProxima = computed(() => proximaCita(this.citas()));
  readonly puntos = signal<Puntos>({ puntosTotales: 0, logrosObtenidos: 0, logrosEnProgreso: 0 });

  readonly formatoDuracion = formatoDuracion;
  readonly claseProgreso = claseProgreso;
  readonly infoTipo = infoTipoAlerta;
  readonly claseIntensidad = claseIntensidad;
  readonly esPendiente = esPendiente;
  readonly nombreHijo = nombreHijo;
  readonly columnasAlertas = ['tipo', 'mensaje', 'intensidad', 'fecha', 'estado'];

  /** Vínculo ACTIVO, autorizado y con permiso CONFIGURAR_LIMITES */
  readonly puedeConfigurar = computed(() => {
    const h = this.hijo();
    return !!h && puedeConfigurarLimites(h);
  });

  readonly puedeVer = computed(() => {
    const h = this.hijo();
    return !!h && h.idUsuarioHijo != null && relacionActiva(h);
  });
  /** URL para volver aquí después de crear/editar un límite */
  readonly urlVolver = `/familia/hijo/${this.idPadreHijo}`;

  readonly minutosHoy = computed(() => this.resumen()?.minutosHoy ?? 0);
  readonly minutosSemana = computed(() => this.resumen()?.minutosSemana ?? 0);
  readonly porcentaje = computed(() => this.resumen()?.porcentajeHoy ?? null);
  readonly limiteDiarioMin = computed(() => this.resumen()?.limiteDiarioMinutos ?? null);
  readonly alertasOrdenadas = computed(() => ordenarAlertas(this.alertas()));
  readonly pendientes = computed(() => this.alertas().filter(esPendiente).length);
  /** Límite ACTIVO del hijo (solo puede haber uno) */
  readonly limiteActivo = computed(() => this.limites().find((l) => l.estado === 'ACTIVO') ?? null);
  /** Activo primero */
  readonly limitesOrdenados = computed(() =>
    [...this.limites()].sort(
      (a, b) => Number(b.estado === 'ACTIVO') - Number(a.estado === 'ACTIVO'),
    ),
  );
  readonly logros = computed(() =>
    [...this.recompensas()].sort(
      (a, b) =>
        (ORDEN_LOGROS[a.estado ?? ''] ?? 9) - (ORDEN_LOGROS[b.estado ?? ''] ?? 9) ||
        (b.porcentajeProgreso ?? 0) - (a.porcentajeProgreso ?? 0),
    ),
  );

  constructor() {
    const idPadreTutor = this.auth.getIdPadreTutor();
    if (idPadreTutor === null) {
      this.cargando.set(false);
      return;
    }

    // Solo se muestran hijos del propio padre: se busca el vínculo en su lista
    this.padreHijoService
      .porPadre(idPadreTutor)
      .pipe(
        switchMap((vinculos) => {
          const hijo = vinculos.find((v) => v.idPadreHijo === this.idPadreHijo) ?? null;
          this.hijo.set(hijo);
          const idUsuarioHijo = hijo?.idUsuarioHijo;
          if (!hijo || idUsuarioHijo == null || !relacionActiva(hijo)) {
            return of(null);
          }
          return forkJoin({
            resumen: this.sesionService.resumen(idUsuarioHijo),
            sesiones: this.sesionService.porUsuario(idUsuarioHijo),
            alertas: this.alertaService.porUsuario(idUsuarioHijo),
            limites: this.limiteService.porUsuario(idUsuarioHijo),
            recompensas: this.recompensaService.porUsuario(idUsuarioHijo),
            puntos: this.recompensaService.puntos(idUsuarioHijo),
            // Solo con vínculo activo (si no, el servidor da 403). Es un extra: si falla, no rompe el detalle
            citas: this.citaService
              .porJugador(hijo.idJugadorJoven)
              .pipe(catchError(() => of([] as Cita[]))),
          });
        }),
      )
      .subscribe({
        next: (datos) => {
          if (datos) {
            this.resumen.set(datos.resumen);
            this.sesiones.set(datos.sesiones);
            this.alertas.set(datos.alertas);
            this.limites.set(datos.limites);
            this.recompensas.set(datos.recompensas);
            this.puntos.set(datos.puntos);
            this.citas.set(datos.citas);
          }
          this.cargando.set(false);
        },
        error: (err) => {
          this.cargando.set(false);
          this.errorCarga.set(true);
          this.notificacion.error(err);
        },
      });
  }

  hora(valor?: string | null): string {
    return valor ? valor.slice(0, 5) : '—';
  }

  relacion(tipo?: string | null): string {
    return TIPOS_RELACION.find((t) => t.valor === tipo)?.etiqueta ?? 'Vínculo';
  }

  metodo(valor?: string | null): string {
    return METODOS_BLOQUEO.find((m) => m.valor === valor)?.etiqueta ?? '—';
  }

  accion(valor?: string | null): string {
    return ACCIONES_AL_EXCEDER.find((a) => a.valor === valor)?.etiqueta ?? '—';
  }

  /** "Configurado por": el padre logueado o el propio hijo */
  creador(limite: LimiteJuego): string {
    if (limite.idCreadoPor === this.auth.getIdUsuario()) return 'Configurado por ti';
    if (limite.idCreadoPor === this.hijo()?.idUsuarioHijo) return 'Configurado por tu hijo/a';
    return 'Configurado por otro tutor';
  }

  etiquetaLogro(estado?: string | null): string {
    return ESTADOS_RECOMPENSA.find((e) => e.valor === estado)?.etiqueta ?? '—';
  }

  claseLogro(estado?: string | null): string {
    if (estado === 'OBTENIDA') return 'exito';
    if (estado === 'RECLAMADA') return 'azul';
    return '';
  }

  /** Configurar el límite: edita el ACTIVO si existe; si no, crea uno nuevo para el hijo */
  configurarLimite(): void {
    if (!this.puedeConfigurar()) return;
    const activo = this.limiteActivo();
    const volver = { volver: this.urlVolver };
    if (activo?.idLimite != null) {
      this.router.navigate(['/limites/editar', activo.idLimite], { queryParams: volver });
    } else {
      this.router.navigate(['/limites/nuevo'], {
        queryParams: { usuario: this.hijo()?.idUsuarioHijo, ...volver },
      });
    }
  }

  /** POST /reportes-padre/generar/{idPadreHijo}?periodo=… y abre el reporte nuevo */
  generarReporte(periodo: PeriodoReporte): void {
    this.generando.set(true);
    this.reporteService.generar(this.idPadreHijo, periodo).subscribe({
      next: (nuevo) => {
        this.generando.set(false);
        this.notificacion.exito('Reporte generado');
        this.router.navigate(['/reportes', nuevo.idReporte]);
      },
      error: (err) => {
        this.generando.set(false);
        this.notificacion.error(err, 'No se pudo generar el reporte');
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
