import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Videojuego } from '../models/videojuego';

// /videojuegos: GET cualquier autenticado; escritura solo ADMIN
@Injectable({ providedIn: 'root' })
export class VideojuegoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/videojuegos`;

  listar(): Observable<Videojuego[]> {
    return this.http.get<Videojuego[]>(this.url);
  }

  obtener(id: number): Observable<Videojuego> {
    return this.http.get<Videojuego>(`${this.url}/${id}`);
  }

  crear(videojuego: Videojuego): Observable<Videojuego> {
    return this.http.post<Videojuego>(this.url, videojuego);
  }

  actualizar(id: number, videojuego: Videojuego): Observable<Videojuego> {
    return this.http.put<Videojuego>(`${this.url}/${id}`, videojuego);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
