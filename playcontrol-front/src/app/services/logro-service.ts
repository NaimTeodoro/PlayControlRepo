import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Logro } from '../models/logro';

// /logros: catálogo (GET cualquier autenticado; escritura solo ADMIN)
@Injectable({ providedIn: 'root' })
export class LogroService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/logros`;

  listar(): Observable<Logro[]> {
    return this.http.get<Logro[]>(this.url);
  }

  obtener(id: number): Observable<Logro> {
    return this.http.get<Logro>(`${this.url}/${id}`);
  }

  crear(logro: Logro): Observable<Logro> {
    return this.http.post<Logro>(this.url, logro);
  }

  actualizar(id: number, logro: Logro): Observable<Logro> {
    return this.http.put<Logro>(`${this.url}/${id}`, logro);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
