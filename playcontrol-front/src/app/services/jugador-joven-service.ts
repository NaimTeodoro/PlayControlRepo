import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { JugadorJoven } from '../models/jugador-joven';

// /jugadores-jovenes: lectura JUGADOR, PADRE, PROFESIONAL y ADMIN; escritura JUGADOR y ADMIN
@Injectable({ providedIn: 'root' })
export class JugadorJovenService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/jugadores-jovenes`;

  listar(): Observable<JugadorJoven[]> {
    return this.http.get<JugadorJoven[]>(this.url);
  }

  obtener(id: number): Observable<JugadorJoven> {
    return this.http.get<JugadorJoven>(`${this.url}/${id}`);
  }

  /** PUT: el código de vinculación no cambia */
  actualizar(id: number, jugador: JugadorJoven): Observable<JugadorJoven> {
    return this.http.put<JugadorJoven>(`${this.url}/${id}`, jugador);
  }

  /** Perfil de jugador de un usuario (un objeto; 404 si no existe) */
  porUsuario(idUsuario: number): Observable<JugadorJoven> {
    return this.http.get<JugadorJoven>(`${this.url}/usuario/${idUsuario}`);
  }

  /** Busca por código de vinculación PC-XXXXXX (404 si no existe) */
  porCodigo(codigo: string): Observable<JugadorJoven> {
    return this.http.get<JugadorJoven>(`${this.url}/codigo/${encodeURIComponent(codigo)}`);
  }
}
