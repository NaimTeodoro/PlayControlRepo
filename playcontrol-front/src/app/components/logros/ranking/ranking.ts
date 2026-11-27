import { Component, computed, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { JugadorJoven } from '../../../models/jugador-joven';
import { Recompensa } from '../../../models/recompensa';
import { AuthService } from '../../../services/auth-service';
import { JugadorJovenService } from '../../../services/jugador-joven-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { RecompensaService } from '../../../services/recompensa-service';

interface Posicion {
  idUsuario: number;
  codigo: string;
  puntos: number;
  logros: number;
  yo: boolean;
}

// Ranking de puntos. No hay endpoint: se calcula con GET /recompensas + GET /jugadores-jovenes
// (suma de puntosAsignados de OBTENIDA y RECLAMADA por usuario). Por privacidad se muestra el
// código de vinculación ("Jugador PC-J00001") y no el nombre real.
@Component({
  selector: 'app-ranking',
  standalone: false,
  templateUrl: './ranking.html',
  styleUrl: './ranking.css',
})
export class Ranking {
  private auth = inject(AuthService);
  private notificacion = inject(NotificacionService);

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly ranking = signal<Posicion[]>([]);
  readonly posicion = computed(() => this.ranking().findIndex((p) => p.yo) + 1);
  readonly mio = computed(() => this.ranking().find((p) => p.yo) ?? null);

  constructor() {
    forkJoin({
      recompensas: inject(RecompensaService).listar(),
      jugadores: inject(JugadorJovenService).listar(),
    }).subscribe({
      next: ({ recompensas, jugadores }) => {
        this.ranking.set(this.calcular(recompensas, jugadores));
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  private calcular(recompensas: Recompensa[], jugadores: JugadorJoven[]): Posicion[] {
    const idActual = this.auth.getIdUsuario();
    const lista = jugadores.map((j) => {
      const ganadas = recompensas.filter(
        (r) => r.idUsuario === j.idUsuario && (r.estado === 'OBTENIDA' || r.estado === 'RECLAMADA'),
      );
      return {
        idUsuario: j.idUsuario,
        codigo: j.codigoVinculacion ?? `#${j.idJugadorJoven}`,
        puntos: ganadas.reduce((t, r) => t + (r.puntosAsignados ?? 0), 0),
        logros: ganadas.length,
        yo: j.idUsuario === idActual,
      };
    });
    return lista.sort((a, b) => b.puntos - a.puntos || a.codigo.localeCompare(b.codigo));
  }

  medalla(indice: number): string | null {
    return ['🥇', '🥈', '🥉'][indice] ?? null;
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
