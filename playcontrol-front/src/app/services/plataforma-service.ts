import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Plataforma } from '../models/plataforma';

// /plataformas: GET cualquier autenticado; escritura solo ADMIN
@Injectable({ providedIn: 'root' })
export class PlataformaService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/plataformas`;

  listar(): Observable<Plataforma[]> {
    return this.http.get<Plataforma[]>(this.url);
  }

  obtener(id: number): Observable<Plataforma> {
    return this.http.get<Plataforma>(`${this.url}/${id}`);
  }

  crear(plataforma: Plataforma): Observable<Plataforma> {
    return this.http.post<Plataforma>(this.url, plataforma);
  }

  /** PUT reemplaza el registro completo: enviar siempre el objeto entero */
  actualizar(id: number, plataforma: Plataforma): Observable<Plataforma> {
    return this.http.put<Plataforma>(`${this.url}/${id}`, plataforma);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
