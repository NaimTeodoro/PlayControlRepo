import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Puntos } from '../models/puntos';
import { Recompensa } from '../models/recompensa';

// /recompensas: progreso de cada usuario en cada logro (JUGADOR, PADRE y ADMIN)
@Injectable({ providedIn: 'root' })
export class RecompensaService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/recompensas`;

  /** Todas las recompensas (para calcular el ranking) */
  listar(): Observable<Recompensa[]> {
    return this.http.get<Recompensa[]>(this.url);
  }

  porUsuario(idUsuario: number): Observable<Recompensa[]> {
    return this.http.get<Recompensa[]>(`${this.url}/usuario/${idUsuario}`);
  }

  puntos(idUsuario: number): Observable<Puntos> {
    return this.http.get<Puntos>(`${this.url}/usuario/${idUsuario}/puntos`);
  }

  /** PUT /{id}/reclamar: solo si está OBTENIDA (si no → 409) */
  reclamar(idRecompensa: number): Observable<Recompensa> {
    return this.http.put<Recompensa>(`${this.url}/${idRecompensa}/reclamar`, null);
  }
}
