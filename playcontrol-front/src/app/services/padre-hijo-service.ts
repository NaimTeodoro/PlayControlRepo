import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PadreHijo, Vincular } from '../models/padre-hijo';

/** El vínculo está autorizado por el hijo y el padre puede ver sus datos */
export function relacionActiva(r: PadreHijo): boolean {
  return r.estado === 'ACTIVO';
}

/** El padre puede configurar límites: vínculo ACTIVO, autorizado y con CONFIGURAR_LIMITES */
export function puedeConfigurarLimites(r: PadreHijo): boolean {
  return (
    relacionActiva(r) &&
    r.autorizado === true &&
    !!r.permisosSupervision?.split(',').some((p) => p.trim() === 'CONFIGURAR_LIMITES')
  );
}

/** "Nombre Apellido" del hijo (o su username) */
export function nombreHijo(r: PadreHijo): string {
  const nombre = [r.nombreHijo, r.apellidoHijo].filter((p) => !!p).join(' ');
  return nombre || r.usernameHijo || 'Hijo/a';
}

// /padre-hijo (JUGADOR, PADRE y ADMIN). Las listas traen idUsuarioHijo y los datos del hijo.
@Injectable({ providedIn: 'root' })
export class PadreHijoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/padre-hijo`;

  obtener(id: number): Observable<PadreHijo> {
    return this.http.get<PadreHijo>(`${this.url}/${id}`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  /** Hijos vinculados al padre (ACTIVO y PENDIENTE) */
  porPadre(idPadreTutor: number): Observable<PadreHijo[]> {
    return this.http.get<PadreHijo[]>(`${this.url}/padre/${idPadreTutor}`);
  }

  /** Vínculos del jugador (ahí ve las solicitudes PENDIENTE) */
  porJoven(idJugadorJoven: number): Observable<PadreHijo[]> {
    return this.http.get<PadreHijo[]>(`${this.url}/joven/${idJugadorJoven}`);
  }

  /** El padre ingresa el código del hijo → PENDIENTE (404 código inexistente, 409 ya vinculados) */
  vincular(datos: Vincular): Observable<PadreHijo> {
    return this.http.post<PadreHijo>(`${this.url}/vincular`, datos);
  }

  /** El hijo autoriza la solicitud → ACTIVO */
  autorizar(idPadreHijo: number): Observable<PadreHijo> {
    return this.http.put<PadreHijo>(`${this.url}/${idPadreHijo}/autorizar`, null);
  }
}
