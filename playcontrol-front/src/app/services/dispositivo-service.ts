import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Dispositivo } from '../models/dispositivo';

@Injectable({ providedIn: 'root' })
export class DispositivoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/dispositivos`;

  listar(): Observable<Dispositivo[]> {
    return this.http.get<Dispositivo[]>(this.url);
  }

  obtener(id: number): Observable<Dispositivo> {
    return this.http.get<Dispositivo>(`${this.url}/${id}`);
  }

  crear(dispositivo: Dispositivo): Observable<Dispositivo> {
    return this.http.post<Dispositivo>(this.url, dispositivo);
  }

  /** PUT reemplaza el registro completo: enviar siempre el objeto entero */
  actualizar(id: number, dispositivo: Dispositivo): Observable<Dispositivo> {
    return this.http.put<Dispositivo>(`${this.url}/${id}`, dispositivo);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  porUsuario(idUsuario: number): Observable<Dispositivo[]> {
    return this.http.get<Dispositivo[]>(`${this.url}/usuario/${idUsuario}`);
  }
}
