import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Cita } from '../models/cita';

/** "Nombre Apellido" del profesional */
export function nombreProfesional(c: Cita): string {
  return [c.nombreProfesional, c.apellidoProfesional].filter((p) => !!p).join(' ') || 'Profesional';
}

/** "Nombre Apellido" del jugador de la cita */
export function nombreJugadorCita(c: Cita): string {
  return [c.nombreJugador, c.apellidoJugador].filter((p) => !!p).join(' ') || 'Jugador';
}

/** Días de calendario desde hoy hasta la fecha (negativo si ya pasó). null si no hay fecha */
export function diasHasta(fecha?: string | null): number | null {
  if (!fecha) return null;
  const destino = new Date(fecha.slice(0, 19)); // ISO sin zona: se interpreta como hora local
  if (isNaN(destino.getTime())) return null;
  const hoy = new Date();
  const inicioHoy = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate());
  const inicioDestino = new Date(destino.getFullYear(), destino.getMonth(), destino.getDate());
  return Math.round((inicioDestino.getTime() - inicioHoy.getTime()) / 86_400_000);
}

/** Texto corto sobre cuánto falta para la cita */
export function etiquetaFaltan(c: Cita): string {
  const dias = diasHasta(c.proximaConsulta);
  if (dias === null) return 'Sin fecha programada';
  if (dias < 0) return 'Fecha pasada · pendiente de reprogramar';
  if (dias === 0) return 'Hoy';
  if (dias === 1) return 'Mañana';
  return `Faltan ${dias} días`;
}

/** Fecha de la cita como Date local (para DatePipe), o null */
export function fechaCita(c: Cita): Date | null {
  return c.proximaConsulta ? new Date(c.proximaConsulta.slice(0, 19)) : null;
}

/** La primera cita con fecha de hoy en adelante, o null */
export function proximaCita(citas: Cita[]): Cita | null {
  const futuras = citas.filter((c) => {
    const dias = diasHasta(c.proximaConsulta);
    return dias !== null && dias >= 0;
  });
  futuras.sort((a, b) => (a.proximaConsulta as string).localeCompare(b.proximaConsulta as string));
  return futuras[0] ?? null;
}

// /citas (JUGADOR, PADRE y ADMIN), solo lectura. Vienen ordenadas por fecha ascendente (sin fecha al final)
@Injectable({ providedIn: 'root' })
export class CitaService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/citas`;

  /** Jugador: sus citas. Padre: las de todos sus hijos con vínculo activo */
  mias(): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.url}/mias`);
  }

  /** Citas de un jugador (id de perfil). El padre solo de hijos con vínculo activo; si no, 403 */
  porJugador(idJugadorJoven: number): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.url}/jugador/${idJugadorJoven}`);
  }
}
