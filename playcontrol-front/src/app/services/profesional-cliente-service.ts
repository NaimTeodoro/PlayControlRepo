import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ProfesionalCliente } from '../models/profesional-cliente';

/** "Nombre Apellido" del jugador cliente */
export function nombreCliente(c: ProfesionalCliente): string {
  return [c.nombreJugador, c.apellidoJugador].filter((p) => !!p).join(' ') || 'Jugador';
}

// /profesional-clientes (PROFESIONAL y ADMIN)
@Injectable({ providedIn: 'root' })
export class ProfesionalClienteService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/profesional-clientes`;

  obtener(id: number): Observable<ProfesionalCliente> {
    return this.http.get<ProfesionalCliente>(`${this.url}/${id}`);
  }

  /** POST: por defecto ACTIVO y fechaAsociacion = ahora (409 si el par ya existe) */
  crear(cliente: ProfesionalCliente): Observable<ProfesionalCliente> {
    return this.http.post<ProfesionalCliente>(this.url, cliente);
  }

  actualizar(id: number, cliente: ProfesionalCliente): Observable<ProfesionalCliente> {
    return this.http.put<ProfesionalCliente>(`${this.url}/${id}`, cliente);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  /** Clientes del profesional con nombreJugador, apellidoJugador e idUsuarioJugador */
  porProfesional(idProfesional: number): Observable<ProfesionalCliente[]> {
    return this.http.get<ProfesionalCliente[]>(`${this.url}/profesional/${idProfesional}`);
  }
}
