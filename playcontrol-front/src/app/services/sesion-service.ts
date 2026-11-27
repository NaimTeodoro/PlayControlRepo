import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ResumenSesiones } from '../models/resumen-sesiones';
import { SesionJuego } from '../models/sesion-juego';

// ---------- Utilidades de fechas y duración (el backend manda ISO sin zona) ----------

const pad = (n: number): string => String(n).padStart(2, '0');

/** Fecha local yyyy-MM-dd */
export function fechaLocal(d: Date): string {
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
}

/** Fecha y hora local sin zona (yyyy-MM-ddTHH:mm:ss), formato que espera el backend */
export function isoLocal(d: Date = new Date()): string {
  return `${fechaLocal(d)}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

/** Convierte un ISO del backend (puede traer 6 decimales) en Date local */
export function aFecha(iso: string): Date {
  return new Date(iso.slice(0, 19));
}

/** 85 → "1 h 25 min" */
export function formatoDuracion(minutos: number): string {
  const total = Math.max(0, Math.round(minutos));
  const h = Math.floor(total / 60);
  const m = total % 60;
  if (h === 0) return `${m} min`;
  return m === 0 ? `${h} h` : `${h} h ${m} min`;
}

/** Minutos de una sesión; si sigue EN_CURSO, el tiempo transcurrido hasta ahora */
export function minutosSesion(s: SesionJuego): number {
  if (s.duracionMinutos != null) return s.duracionMinutos;
  if (s.estadoSesion === 'EN_CURSO') {
    return Math.max(0, Math.round((Date.now() - aFecha(s.fechaInicio).getTime()) / 60000));
  }
  return 0;
}

export interface DiaTiempo {
  fecha: string; // yyyy-MM-dd
  etiqueta: string; // L, M, X...
  minutos: number;
}

const LETRAS_DIA = ['D', 'L', 'M', 'X', 'J', 'V', 'S'];

/** Minutos jugados por día en los últimos `dias` días (el último es hoy), para el gráfico */
export function minutosPorDia(
  sesiones: SesionJuego[],
  dias = 14,
  ahora: Date = new Date(),
): DiaTiempo[] {
  const porDia = new Map<string, number>();
  for (const s of sesiones) {
    const dia = s.fechaInicio.slice(0, 10);
    porDia.set(dia, (porDia.get(dia) ?? 0) + minutosSesion(s));
  }
  const resultado: DiaTiempo[] = [];
  for (let i = dias - 1; i >= 0; i--) {
    const d = new Date(ahora);
    d.setDate(ahora.getDate() - i);
    const fecha = fechaLocal(d);
    resultado.push({ fecha, etiqueta: LETRAS_DIA[d.getDay()], minutos: porDia.get(fecha) ?? 0 });
  }
  return resultado;
}

// /sesiones: solo una EN_CURSO por usuario (otra → 409)
@Injectable({ providedIn: 'root' })
export class SesionService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/sesiones`;

  obtener(id: number): Observable<SesionJuego> {
    return this.http.get<SesionJuego>(`${this.url}/${id}`);
  }

  crear(sesion: SesionJuego): Observable<SesionJuego> {
    return this.http.post<SesionJuego>(this.url, sesion);
  }

  actualizar(id: number, sesion: SesionJuego): Observable<SesionJuego> {
    return this.http.put<SesionJuego>(`${this.url}/${id}`, sesion);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  /** Sesiones del usuario, la más reciente primero */
  porUsuario(idUsuario: number): Observable<SesionJuego[]> {
    return this.http.get<SesionJuego[]>(`${this.url}/usuario/${idUsuario}`);
  }

  /** Minutos de hoy / semana / mes, % del límite diario ACTIVO y sesión en curso */
  resumen(idUsuario: number): Observable<ResumenSesiones> {
    return this.http.get<ResumenSesiones>(`${this.url}/usuario/${idUsuario}/resumen`);
  }
}
