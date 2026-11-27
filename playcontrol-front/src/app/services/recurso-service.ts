import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Audiencia, RecursoEducativo, TIPOS_RECURSO } from '../models/recurso-educativo';

/** Etiqueta e icono del tipo de recurso */
export function infoTipoRecurso(tipo?: string | null): { etiqueta: string; icono: string } {
  return (
    TIPOS_RECURSO.find((t) => t.valor === tipo) ?? {
      etiqueta: tipo ?? 'Recurso',
      icono: 'menu_book',
    }
  );
}

export interface FiltroRecursos {
  categoria?: string; // FAQ = categoria "FAQ"
  audiencia?: Audiencia;
}

// /recursos-educativos: GET cualquier autenticado; escritura PROFESIONAL y ADMIN
@Injectable({ providedIn: 'root' })
export class RecursoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/recursos-educativos`;

  /** Filtros opcionales ?categoria= y ?audiencia= (sin distinguir mayúsculas) */
  listar(filtro: FiltroRecursos = {}): Observable<RecursoEducativo[]> {
    let params = new HttpParams();
    if (filtro.categoria) params = params.set('categoria', filtro.categoria);
    if (filtro.audiencia) params = params.set('audiencia', filtro.audiencia);
    return this.http.get<RecursoEducativo[]>(this.url, { params });
  }

  obtener(id: number): Observable<RecursoEducativo> {
    return this.http.get<RecursoEducativo>(`${this.url}/${id}`);
  }

  crear(recurso: RecursoEducativo): Observable<RecursoEducativo> {
    return this.http.post<RecursoEducativo>(this.url, recurso);
  }

  actualizar(id: number, recurso: RecursoEducativo): Observable<RecursoEducativo> {
    return this.http.put<RecursoEducativo>(`${this.url}/${id}`, recurso);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
