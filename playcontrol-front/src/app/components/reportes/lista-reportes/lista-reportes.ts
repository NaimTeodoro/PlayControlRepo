import { Component, computed, effect, inject, signal, viewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { PadreHijo } from '../../../models/padre-hijo';
import { PERIODOS_REPORTE, PeriodoReporte, ReportePadre } from '../../../models/reporte-padre';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { nombreHijo, PadreHijoService, relacionActiva } from '../../../services/padre-hijo-service';
import {
  claseTendencia,
  etiquetaPeriodo,
  infoTendencia,
  ReportePadreService,
} from '../../../services/reporte-padre-service';
import { formatoDuracion } from '../../../services/sesion-service';

// Reportes del padre (GET /reportes-padre/padre/{idPadreTutor}): filtro por hijo (?hijo=idPadreHijo),
// marcar como visto y generar uno nuevo SEMANAL o MENSUAL para un vínculo ACTIVO.
@Component({
  selector: 'app-lista-reportes',
  standalone: false,
  templateUrl: './lista-reportes.html',
  styleUrl: './lista-reportes.css',
})
export class ListaReportes {
  private auth = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private reporteService = inject(ReportePadreService);
  private padreHijoService = inject(PadreHijoService);
  private notificacion = inject(NotificacionService);

  readonly idPadreTutor = this.auth.getIdPadreTutor();
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly generando = signal(false);
  readonly marcando = signal<number | null>(null);

  readonly reportes = signal<ReportePadre[]>([]);
  readonly vinculos = signal<PadreHijo[]>([]);
  /** idPadreHijo del filtro (null = todos los hijos) */
  readonly filtroHijo = signal<number | null>(
    Number(this.route.snapshot.queryParamMap.get('hijo')) || null,
  );
  readonly soloNoVistos = signal(false);

  // Formulario de generación
  readonly periodos = PERIODOS_REPORTE;
  readonly hijoGenerar = signal<number | null>(null);
  readonly periodoGenerar = signal<PeriodoReporte>('SEMANAL');

  readonly columnas = ['hijo', 'periodo', 'tiempo', 'respeto', 'tendencia', 'visto', 'acciones'];
  readonly datos = new MatTableDataSource<ReportePadre>([]);
  private paginador = viewChild(MatPaginator);

  readonly nombreHijo = nombreHijo;
  readonly infoTendencia = infoTendencia;
  readonly claseTendencia = claseTendencia;
  readonly etiquetaPeriodo = etiquetaPeriodo;
  readonly formatoDuracion = formatoDuracion;

  /** Solo los vínculos ACTIVO pueden generar reportes (si no → 409) */
  readonly hijosActivos = computed(() => this.vinculos().filter(relacionActiva));

  readonly filtrados = computed(() => {
    const hijo = this.filtroHijo();
    return this.reportes().filter(
      (r) => (hijo === null || r.idPadreHijo === hijo) && (!this.soloNoVistos() || !r.visto),
    );
  });
  readonly noVistos = computed(() => this.reportes().filter((r) => !r.visto).length);

  constructor() {
    effect(() => (this.datos.data = this.filtrados()));
    effect(() => {
      const paginador = this.paginador();
      if (paginador) this.datos.paginator = paginador;
    });
    this.cargar();
  }

  private cargar(): void {
    if (this.idPadreTutor === null) {
      this.cargando.set(false);
      return;
    }
    forkJoin({
      reportes: this.reporteService.porPadre(this.idPadreTutor),
      vinculos: this.padreHijoService.porPadre(this.idPadreTutor),
    }).subscribe({
      next: ({ reportes, vinculos }) => {
        this.reportes.set(reportes);
        this.vinculos.set(vinculos);
        // Por defecto se genera para el hijo filtrado o el primer hijo activo
        const activos = vinculos.filter(relacionActiva);
        const preferido = activos.find((v) => v.idPadreHijo === this.filtroHijo()) ?? activos[0];
        this.hijoGenerar.set(preferido?.idPadreHijo ?? null);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  /** Nombre del hijo del reporte (el DTO ya trae nombreHijo; si no, se busca en los vínculos) */
  nombreDe(r: ReportePadre): string {
    if (r.nombreHijo) return r.nombreHijo;
    const v = this.vinculos().find((x) => x.idPadreHijo === r.idPadreHijo);
    return v ? nombreHijo(v) : 'Hijo/a';
  }

  cambiarFiltro(idPadreHijo: number | null): void {
    this.filtroHijo.set(idPadreHijo);
    // Se refleja en la URL para poder volver al mismo filtro
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { hijo: idPadreHijo ?? null },
      replaceUrl: true,
    });
  }

  marcarVisto(reporte: ReportePadre): void {
    if (reporte.visto || reporte.idReporte == null) return;
    this.marcando.set(reporte.idReporte);
    this.reporteService.marcarVisto(reporte).subscribe({
      next: (actualizado) => {
        this.reportes.update((lista) =>
          lista.map((r) => (r.idReporte === actualizado.idReporte ? actualizado : r)),
        );
        this.marcando.set(null);
        this.notificacion.exito('Reporte marcado como visto');
      },
      error: (err) => {
        this.marcando.set(null);
        this.notificacion.error(err, 'No se pudo marcar el reporte');
      },
    });
  }

  generar(): void {
    const idPadreHijo = this.hijoGenerar();
    if (idPadreHijo === null) return;
    this.generando.set(true);
    this.reporteService.generar(idPadreHijo, this.periodoGenerar()).subscribe({
      next: (nuevo) => {
        this.generando.set(false);
        this.notificacion.exito('Reporte generado');
        // Se abre directamente el detalle del reporte nuevo
        this.router.navigate(['/reportes', nuevo.idReporte]);
      },
      error: (err) => {
        this.generando.set(false);
        // 409: el vínculo no está ACTIVO (el backend envía el detalle)
        this.notificacion.error(err, 'No se pudo generar el reporte');
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
