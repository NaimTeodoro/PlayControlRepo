import { Component, computed, inject, signal } from '@angular/core';
import { catchError, forkJoin, map, of, switchMap } from 'rxjs';
import { Cita } from '../../../models/cita';
import { PadreHijo } from '../../../models/padre-hijo';
import { ReportePadre } from '../../../models/reporte-padre';
import { AlertaService } from '../../../services/alerta-service';
import { AuthService } from '../../../services/auth-service';
import { CitaService, diasHasta, fechaCita, nombreProfesional, proximaCita } from '../../../services/cita-service';
import { claseProgreso } from '../../../services/limite-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { nombreHijo, PadreHijoService, relacionActiva } from '../../../services/padre-hijo-service';
import { etiquetaPeriodo, ReportePadreService } from '../../../services/reporte-padre-service';
import { formatoDuracion, SesionService } from '../../../services/sesion-service';

interface ResumenHijo {
  hijo: PadreHijo;
  nombre: string;
  activo: boolean; // vínculo autorizado por el hijo
  minutosHoy: number;
  minutosSemana: number;
  limiteMinutos: number | null;
  porcentaje: number | null;
  alertasPendientes: number;
  enCurso: boolean;
  reporteNuevo: ReportePadre | null; // último reporte sin ver
}

// Inicio del padre: una tarjeta por hijo con el resumen del backend (con su idUsuarioHijo)
// y sus alertas pendientes, más el último reporte sin ver de cada hijo
@Component({
  selector: 'app-inicio-padre',
  standalone: false,
  templateUrl: './inicio-padre.html',
  styleUrl: './inicio-padre.css',
})
export class InicioPadre {
  private auth = inject(AuthService);
  private padreHijoService = inject(PadreHijoService);
  private sesionService = inject(SesionService);
  private alertaService = inject(AlertaService);
  private reporteService = inject(ReportePadreService);
  private citaService = inject(CitaService);
  private notificacion = inject(NotificacionService);

  readonly sesion = this.auth.sesionActual;
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly sinPerfil = signal(false);
  readonly resumenes = signal<ResumenHijo[]>([]);

  readonly formatoDuracion = formatoDuracion;
  readonly claseProgreso = claseProgreso;
  readonly etiquetaPeriodo = etiquetaPeriodo;
  readonly fechaCita = fechaCita;
  readonly nombreProfesional = nombreProfesional;

  readonly totalHijos = computed(() => this.resumenes().length);
  readonly totalPendientes = computed(() =>
    this.resumenes().reduce((t, r) => t + r.alertasPendientes, 0),
  );
  readonly superaronLimite = computed(
    () => this.resumenes().filter((r) => (r.porcentaje ?? 0) >= 100).length,
  );
  readonly reportesSinVer = signal(0);
  private citas = signal<Cita[]>([]);

  /** Citas de los hijos que caen esta semana (próximos 7 días, incluido hoy) */
  readonly citasSemana = computed(
    () =>
      this.citas().filter((c) => {
        const d = diasHasta(c.proximaConsulta);
        return d !== null && d >= 0 && d < 7;
      }).length,
  );

  /** Próxima cita de un hijo (por su id de perfil de jugador) */
  proximaDe(idJugadorJoven: number): Cita | null {
    return proximaCita(this.citas().filter((c) => c.idJugadorJoven === idJugadorJoven));
  }

  constructor() {
    const idPadreTutor = this.auth.getIdPadreTutor();
    if (idPadreTutor === null) {
      this.sinPerfil.set(true);
      this.cargando.set(false);
      return;
    }

    // Las citas son un extra: una sola llamada aparte; si falla, el inicio carga igual
    this.citaService
      .mias()
      .pipe(catchError(() => of([] as Cita[])))
      .subscribe((c) => this.citas.set(c));

    forkJoin({
      vinculos: this.padreHijoService.porPadre(idPadreTutor),
      // Los reportes son un extra: si fallan, el inicio se muestra igual
      reportes: this.reporteService
        .porPadre(idPadreTutor)
        .pipe(catchError(() => of([] as ReportePadre[]))),
    })
      .pipe(
        switchMap(({ vinculos, reportes }) => {
          this.reportesSinVer.set(reportes.filter((r) => !r.visto).length);
          // La lista viene más reciente primero: el primero sin ver de cada vínculo es el último
          const nuevo = (h: PadreHijo) =>
            reportes.find((r) => r.idPadreHijo === h.idPadreHijo && !r.visto) ?? null;
          // Activos primero; los datos de juego solo se piden para vínculos ACTIVO
          const hijos = [...vinculos].sort(
            (a, b) => Number(relacionActiva(b)) - Number(relacionActiva(a)),
          );
          const datos$ = hijos.map((h) =>
            relacionActiva(h) && h.idUsuarioHijo != null
              ? forkJoin({
                  resumen: this.sesionService.resumen(h.idUsuarioHijo),
                  pendientes: this.alertaService.pendientes(h.idUsuarioHijo),
                }).pipe(
                  map((d) => this.armarResumen(h, d.resumen, d.pendientes.length, nuevo(h))),
                )
              : of(this.armarResumen(h, null, 0, null)),
          );
          return datos$.length ? forkJoin(datos$) : of([] as ResumenHijo[]);
        }),
      )
      .subscribe({
        next: (resumenes) => {
          this.resumenes.set(resumenes);
          this.cargando.set(false);
        },
        error: (err) => {
          this.cargando.set(false);
          this.errorCarga.set(true);
          this.notificacion.error(err);
        },
      });
  }

  private armarResumen(
    hijo: PadreHijo,
    resumen: {
      minutosHoy: number;
      minutosSemana: number;
      limiteDiarioMinutos: number | null;
      porcentajeHoy: number | null;
      sesionEnCurso: unknown;
    } | null,
    alertasPendientes: number,
    reporteNuevo: ReportePadre | null,
  ): ResumenHijo {
    return {
      hijo,
      nombre: nombreHijo(hijo),
      activo: relacionActiva(hijo),
      minutosHoy: resumen?.minutosHoy ?? 0,
      minutosSemana: resumen?.minutosSemana ?? 0,
      limiteMinutos: resumen?.limiteDiarioMinutos ?? null,
      porcentaje: resumen?.porcentajeHoy ?? null,
      alertasPendientes,
      enCurso: !!resumen?.sesionEnCurso,
      reporteNuevo,
    };
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
