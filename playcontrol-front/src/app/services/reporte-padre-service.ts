import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PERIODOS_REPORTE, PeriodoReporte, ReportePadre, TENDENCIAS } from '../models/reporte-padre';

/** Etiqueta e icono de la tendencia (MEJORA = menos minutos o más respeto del límite) */
export function infoTendencia(tendencia?: string | null): { etiqueta: string; icono: string } {
  return TENDENCIAS.find((t) => t.valor === tendencia) ?? { etiqueta: '—', icono: 'remove' };
}

/** Clase del chip de tendencia: MEJORA verde, ESTABLE gris, EMPEORA rojo */
export function claseTendencia(tendencia?: string | null): string {
  if (tendencia === 'MEJORA') return 'exito';
  if (tendencia === 'EMPEORA') return 'peligro';
  return '';
}

export function etiquetaPeriodo(periodo?: string | null): string {
  return PERIODOS_REPORTE.find((p) => p.valor === periodo)?.etiqueta ?? '—';
}

// /reportes-padre (PADRE y ADMIN)
@Injectable({ providedIn: 'root' })
export class ReportePadreService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/reportes-padre`;

  obtener(id: number): Observable<ReportePadre> {
    return this.http.get<ReportePadre>(`${this.url}/${id}`);
  }

  /** Reportes de todos los hijos del padre, más recientes primero */
  porPadre(idPadreTutor: number): Observable<ReportePadre[]> {
    return this.http.get<ReportePadre[]>(`${this.url}/padre/${idPadreTutor}`);
  }

  porPadreHijo(idPadreHijo: number): Observable<ReportePadre[]> {
    return this.http.get<ReportePadre[]>(`${this.url}/padre-hijo/${idPadreHijo}`);
  }

  /** Genera un reporte con los datos reales del hijo (409 si el vínculo no está ACTIVO) */
  generar(idPadreHijo: number, periodo: PeriodoReporte = 'SEMANAL'): Observable<ReportePadre> {
    return this.http.post<ReportePadre>(`${this.url}/generar/${idPadreHijo}`, null, {
      params: { periodo },
    });
  }

  /** Marca el reporte como visto (el servidor guarda fechaVisto) */
  marcarVisto(reporte: ReportePadre): Observable<ReportePadre> {
    return this.http.put<ReportePadre>(`${this.url}/${reporte.idReporte}`, {
      ...reporte,
      visto: true,
    });
  }
}
