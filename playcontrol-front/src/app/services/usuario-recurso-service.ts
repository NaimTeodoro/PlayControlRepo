import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioRecursoEducativo } from '../models/usuario-recurso-educativo';

// /usuario-recursos: consumo de recursos educativos (JUGADOR, PADRE y ADMIN)
@Injectable({ providedIn: 'root' })
export class UsuarioRecursoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/usuario-recursos`;

  porUsuario(idUsuario: number): Observable<UsuarioRecursoEducativo[]> {
    return this.http.get<UsuarioRecursoEducativo[]>(`${this.url}/usuario/${idUsuario}`);
  }

  /** POST suma una visita al recurso; 409 si el par usuario-recurso ya existe (usar PUT) */
  crear(consumo: UsuarioRecursoEducativo): Observable<UsuarioRecursoEducativo> {
    return this.http.post<UsuarioRecursoEducativo>(this.url, consumo);
  }

  actualizar(id: number, consumo: UsuarioRecursoEducativo): Observable<UsuarioRecursoEducativo> {
    return this.http.put<UsuarioRecursoEducativo>(`${this.url}/${id}`, consumo);
  }
}
