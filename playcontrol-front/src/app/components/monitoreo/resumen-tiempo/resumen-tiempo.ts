import { Component, computed, inject, signal } from '@angular/core';
import { LimiteJuego } from '../../../models/limite-juego';
import { ResumenSesiones } from '../../../models/resumen-sesiones';
import { SesionJuego } from '../../../models/sesion-juego';
import { AuthService } from '../../../services/auth-service';
import { claseProgreso, LimiteService, porcentajeLimite } from '../../../services/limite-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { formatoDuracion, minutosPorDia, SesionService } from '../../../services/sesion-service';
import { forkJoin } from 'rxjs';

interface TarjetaPeriodo {
  titulo: string;
  icono: string;
  minutos: number;
  limiteHoras?: number;
  porcentaje: number | null;
}

// Pantalla "Monitoreo": resumen de tiempo (GET /sesiones/usuario/{id}/resumen), gráfico de los
// últimos 14 días (calculado con las sesiones del usuario) y lista de sesiones
@Component({
  selector: 'app-resumen-tiempo',
  standalone: false,
  templateUrl: './resumen-tiempo.html',
  styleUrl: './resumen-tiempo.css',
})
export class ResumenTiempo {
  private auth = inject(AuthService);
  private sesionService = inject(SesionService);
  private limiteService = inject(LimiteService);
  private notificacion = inject(NotificacionService);

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  readonly sesiones = signal<SesionJuego[]>([]);
  readonly limite = signal<LimiteJuego | null>(null);
  private resumen = signal<ResumenSesiones | null>(null);

  readonly formatoDuracion = formatoDuracion;
  readonly claseProgreso = claseProgreso;

  // Hoy usa el % que calcula el backend; semana y mes se comparan con el límite ACTIVO
  readonly tarjetas = computed<TarjetaPeriodo[]>(() => {
    const r = this.resumen();
    const l = this.limite();
    const semana = r?.minutosSemana ?? 0;
    const mes = r?.minutosMes ?? 0;
    return [
      {
        titulo: 'Hoy',
        icono: 'today',
        minutos: r?.minutosHoy ?? 0,
        limiteHoras: r?.limiteDiarioMinutos != null ? r.limiteDiarioMinutos / 60 : undefined,
        porcentaje: r?.porcentajeHoy ?? null,
      },
      {
        titulo: 'Esta semana',
        icono: 'date_range',
        minutos: semana,
        limiteHoras: l?.limiteHorasSemanal ?? undefined,
        porcentaje: porcentajeLimite(semana, l?.limiteHorasSemanal),
      },
      {
        titulo: 'Este mes',
        icono: 'calendar_month',
        minutos: mes,
        limiteHoras: l?.limiteHorasMensual ?? undefined,
        porcentaje: porcentajeLimite(mes, l?.limiteHorasMensual),
      },
    ];
  });

  readonly enCurso = computed(() => this.resumen()?.sesionEnCurso ?? null);
  readonly ultimosDias = computed(() => minutosPorDia(this.sesiones(), 14));
  readonly limiteDiarioMin = computed(() => this.resumen()?.limiteDiarioMinutos ?? null);

  // Altura de cada barra del gráfico (proporcional al día con más tiempo o al límite diario)
  readonly maximoGrafico = computed(() =>
    Math.max(60, this.limiteDiarioMin() ?? 0, ...this.ultimosDias().map((d) => d.minutos)),
  );

  constructor() {
    const id = this.auth.getIdUsuario();
    if (id === null) return;

    forkJoin({
      resumen: this.sesionService.resumen(id),
      sesiones: this.sesionService.porUsuario(id),
      limites: this.limiteService.porUsuario(id),
    }).subscribe({
      next: ({ resumen, sesiones, limites }) => {
        this.resumen.set(resumen);
        this.sesiones.set(sesiones);
        this.limite.set(limites.find((l) => l.estado === 'ACTIVO') ?? null);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  alturaBarra(minutos: number): number {
    return Math.max(minutos > 0 ? 4 : 0, (minutos / this.maximoGrafico()) * 100);
  }

  /** Barra roja si ese día superó el límite diario */
  superoLimite(minutos: number): boolean {
    const limite = this.limiteDiarioMin();
    return limite !== null && minutos > limite;
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
