import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, forkJoin, map, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { LimiteJuego } from '../models/limite-juego';
import { AuthService } from './auth-service';
import { PadreHijoService } from './padre-hijo-service';

/** Color de la barra de progreso según % del límite: verde < 70, ámbar 70–99, rojo ≥ 100 */
export function claseProgreso(
  porcentaje: number,
): 'progreso-ok' | 'progreso-aviso' | 'progreso-pasado' {
  if (porcentaje >= 100) return 'progreso-pasado';
  return porcentaje >= 70 ? 'progreso-aviso' : 'progreso-ok';
}

/** Porcentaje (sin tope) de minutos usados frente a un límite en horas; null si no hay límite */
export function porcentajeLimite(minutos: number, limiteHoras?: number | null): number | null {
  return limiteHoras && limiteHoras > 0 ? (minutos / (limiteHoras * 60)) * 100 : null;
}

// /limites-juego (JUGADOR, PADRE y ADMIN)
@Injectable({ providedIn: 'root' })
export class LimiteService {
  private http = inject(HttpClient);
  private auth = inject(AuthService);
  private padreHijo = inject(PadreHijoService);
  private url = `${environment.apiUrl}/limites-juego`;

  obtener(id: number): Observable<LimiteJuego> {
    return this.http.get<LimiteJuego>(`${this.url}/${id}`);
  }

  /** POST sin idCreadoPor: el servidor asigna al usuario autenticado */
  crear(limite: LimiteJuego): Observable<LimiteJuego> {
    return this.http.post<LimiteJuego>(this.url, limite);
  }

  actualizar(id: number, limite: LimiteJuego): Observable<LimiteJuego> {
    return this.http.put<LimiteJuego>(`${this.url}/${id}`, limite);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  porUsuario(idUsuario: number): Observable<LimiteJuego[]> {
    return this.http.get<LimiteJuego[]>(`${this.url}/usuario/${idUsuario}`);
  }

  porUsuarioActivos(idUsuario: number): Observable<LimiteJuego[]> {
    return this.http.get<LimiteJuego[]>(`${this.url}/usuario/${idUsuario}/activos`);
  }

  /**
   * ¿El límite del jugador logueado lo controla su padre/tutor? Sí cuando hay un límite ACTIVO
   * con idCreadoPor distinto del propio usuario Y un vínculo ACTIVO con autorizado:true.
   * Único lugar con esta lógica (lista de límites, guard e inicio). Si falla algo, no bloquea:
   * el servidor igual responde 403 con su mensaje.
   */
  estaControladoPorPadre(): Observable<{ controlado: boolean; limite?: LimiteJuego }> {
    const idUsuario = this.auth.getIdUsuario();
    const idJoven = this.auth.getIdJugadorJoven();
    if (idUsuario === null || idJoven === null) return of({ controlado: false });

    return forkJoin({
      limites: this.porUsuarioActivos(idUsuario),
      vinculos: this.padreHijo.porJoven(idJoven),
    }).pipe(
      map(({ limites, vinculos }) => {
        const limite = limites.find(
          (l) => l.estado === 'ACTIVO' && l.idCreadoPor != null && l.idCreadoPor !== idUsuario,
        );
        const vinculado = vinculos.some((v) => v.estado === 'ACTIVO' && v.autorizado === true);
        return limite && vinculado ? { controlado: true, limite } : { controlado: false };
      }),
      catchError(() => of({ controlado: false })),
    );
  }
}
