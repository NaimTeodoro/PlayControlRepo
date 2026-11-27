import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AlertaJuego, TIPOS_ALERTA } from '../models/alerta-juego';

/** Pendiente = aún no respondida (mismo criterio que /alertas-juego/usuario/{id}/pendientes) */
export function esPendiente(a: AlertaJuego): boolean {
  return !a.respondidoUsuario;
}

/** Pendientes primero y, dentro de cada grupo, la más reciente primero */
export function ordenarAlertas(alertas: AlertaJuego[]): AlertaJuego[] {
  return [...alertas].sort((a, b) => {
    const pa = esPendiente(a) ? 0 : 1;
    const pb = esPendiente(b) ? 0 : 1;
    return pa - pb || (b.fechaEnvio ?? '').localeCompare(a.fechaEnvio ?? '');
  });
}

/** Etiqueta e icono del tipo de alerta */
export function infoTipoAlerta(tipo: string): { etiqueta: string; icono: string } {
  return TIPOS_ALERTA.find((t) => t.valor === tipo) ?? { etiqueta: tipo, icono: 'notifications' };
}

/** Clase del chip según la intensidad: ALTA rojo, MEDIA ámbar, BAJA verde */
export function claseIntensidad(intensidad?: string | null): string {
  if (intensidad === 'ALTA') return 'peligro';
  if (intensidad === 'MEDIA') return 'alerta';
  return 'exito';
}

@Injectable({ providedIn: 'root' })
export class AlertaService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/alertas-juego`;

  obtener(id: number): Observable<AlertaJuego> {
    return this.http.get<AlertaJuego>(`${this.url}/${id}`);
  }

  porUsuario(idUsuario: number): Observable<AlertaJuego[]> {
    return this.http.get<AlertaJuego[]>(`${this.url}/usuario/${idUsuario}`);
  }

  pendientes(idUsuario: number): Observable<AlertaJuego[]> {
    return this.http.get<AlertaJuego[]>(`${this.url}/usuario/${idUsuario}/pendientes`);
  }

  /** PUT /{id}/responder: queda RESPONDIDA con tiempoRespuesta (409 si ya estaba respondida) */
  responder(idAlerta: number, respuestaUsuario: string): Observable<AlertaJuego> {
    return this.http.put<AlertaJuego>(`${this.url}/${idAlerta}/responder`, { respuestaUsuario });
  }
}
