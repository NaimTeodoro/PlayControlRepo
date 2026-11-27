import { Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReportePadre } from '../../../models/reporte-padre';
import { infoTipoAlerta } from '../../../services/alerta-service';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import {
  claseTendencia,
  etiquetaPeriodo,
  infoTendencia,
  ReportePadreService,
} from '../../../services/reporte-padre-service';
import { formatoDuracion } from '../../../services/sesion-service';

interface AlertaImportante {
  etiqueta: string;
  icono: string;
  veces: string;
}

// Detalle de un reporte (GET /reportes-padre/{id}). Al abrir uno no visto se marca con PUT visto:true.
@Component({
  selector: 'app-detalle-reporte',
  standalone: false,
  templateUrl: './detalle-reporte.html',
  styleUrl: './detalle-reporte.css',
})
export class DetalleReporte {
  private auth = inject(AuthService);
  private router = inject(Router);
  private reporteService = inject(ReportePadreService);
  private notificacion = inject(NotificacionService);

  private idReporte = Number(inject(ActivatedRoute).snapshot.paramMap.get('id'));

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly generando = signal(false);
  readonly reporte = signal<ReportePadre | null>(null);

  readonly formatoDuracion = formatoDuracion;
  readonly etiquetaPeriodo = etiquetaPeriodo;
  readonly infoTendencia = infoTendencia;
  readonly claseTendencia = claseTendencia;

  /** "Texto 1. Texto 2." → ["Texto 1.", "Texto 2."] para mostrarlas como lista */
  readonly recomendaciones = computed(() =>
    (this.reporte()?.recomendacionesSistema ?? '')
      .split(/(?<=\.)\s+/)
      .map((t) => t.trim())
      .filter((t) => t.length > 0),
  );

  /** "Alertas de intensidad ALTA: HORARIO_NOCTURNO x1, LIMITE_DIARIO x4" → chips por tipo */
  readonly alertasImportantes = computed<AlertaImportante[]>(() => {
    const texto = this.reporte()?.alertasImportantes ?? '';
    const lista = texto.includes(':') ? texto.slice(texto.indexOf(':') + 1) : '';
    return lista
      .split(',')
      .map((p) => p.trim().match(/^([A-Z_]+)\s*x(\d+)$/))
      .filter((m): m is RegExpMatchArray => m !== null)
      .map((m) => ({ ...infoTipoAlerta(m[1]), veces: m[2] }));
  });

  readonly respeto = computed(() => Math.round(this.reporte()?.respetoLimitesPorc ?? 0));

  constructor() {
    this.reporteService.obtener(this.idReporte).subscribe({
      next: (r) => {
        // Solo se muestran los reportes de los hijos del padre logueado
        if (r.idPadreTutor != null && r.idPadreTutor !== this.auth.getIdPadreTutor()) {
          this.cargando.set(false);
          return;
        }
        this.reporte.set(r);
        this.cargando.set(false);
        if (!r.visto) this.marcarVisto(r);
      },
      error: (err) => {
        this.cargando.set(false);
        // 404 → "no encontrado"; otros errores → estado de error con Reintentar
        if (err?.status !== 404) {
          this.errorCarga.set(true);
          this.notificacion.error(err);
        }
      },
    });
  }

  private marcarVisto(r: ReportePadre): void {
    this.reporteService.marcarVisto(r).subscribe({
      next: (actualizado) => this.reporte.set(actualizado),
      // Si falla no se interrumpe la lectura del reporte
      error: () => undefined,
    });
  }

  /** Genera un reporte nuevo del mismo hijo y periodo y lo abre */
  generarOtro(): void {
    const r = this.reporte();
    if (!r) return;
    this.generando.set(true);
    this.reporteService.generar(r.idPadreHijo, r.periodoReporte ?? 'SEMANAL').subscribe({
      next: (nuevo) => {
        this.generando.set(false);
        this.notificacion.exito('Reporte generado');
        this.router.navigate(['/reportes', nuevo.idReporte]).then(() => this.recargarCon(nuevo));
      },
      error: (err) => {
        this.generando.set(false);
        this.notificacion.error(err, 'No se pudo generar el reporte');
      },
    });
  }

  /** Al navegar a la misma ruta Angular reutiliza el componente: se pinta el nuevo reporte */
  private recargarCon(nuevo: ReportePadre): void {
    this.idReporte = nuevo.idReporte as number;
    this.reporte.set(nuevo);
    if (!nuevo.visto) this.marcarVisto(nuevo);
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
