import { Component, computed, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Puntos } from '../../../models/puntos';
import { ESTADOS_RECOMPENSA, Recompensa } from '../../../models/recompensa';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { RecompensaService } from '../../../services/recompensa-service';

// Orden de las tarjetas: primero las que se pueden reclamar, luego en progreso y al final las reclamadas
const ORDEN: Record<string, number> = { OBTENIDA: 0, EN_PROGRESO: 1, RECLAMADA: 2 };

// Logros del jugador: progreso en cada logro (GET /recompensas/usuario/{id}), puntos y "Reclamar"
@Component({
  selector: 'app-lista-logros',
  standalone: false,
  templateUrl: './lista-logros.html',
  styleUrl: './lista-logros.css',
})
export class ListaLogros {
  private auth = inject(AuthService);
  private recompensaService = inject(RecompensaService);
  private notificacion = inject(NotificacionService);

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly reclamando = signal<number | null>(null);
  readonly puntos = signal<Puntos>({ puntosTotales: 0, logrosObtenidos: 0, logrosEnProgreso: 0 });
  private recompensas = signal<Recompensa[]>([]);
  // Imágenes que no cargaron (se muestra un icono en su lugar)
  readonly imagenesRotas = signal<Set<number>>(new Set());

  readonly logros = computed(() =>
    [...this.recompensas()].sort(
      (a, b) =>
        (ORDEN[a.estado ?? ''] ?? 9) - (ORDEN[b.estado ?? ''] ?? 9) ||
        (b.porcentajeProgreso ?? 0) - (a.porcentajeProgreso ?? 0),
    ),
  );
  readonly porReclamar = computed(
    () => this.recompensas().filter((r) => r.estado === 'OBTENIDA').length,
  );

  constructor() {
    this.cargar();
  }

  private cargar(): void {
    const id = this.auth.getIdUsuario();
    if (id === null) return;
    forkJoin({
      recompensas: this.recompensaService.porUsuario(id),
      puntos: this.recompensaService.puntos(id),
    }).subscribe({
      next: ({ recompensas, puntos }) => {
        this.recompensas.set(recompensas);
        this.puntos.set(puntos);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  etiquetaEstado(estado?: string): string {
    return ESTADOS_RECOMPENSA.find((e) => e.valor === estado)?.etiqueta ?? '—';
  }

  claseEstado(estado?: string): string {
    if (estado === 'OBTENIDA') return 'exito';
    if (estado === 'RECLAMADA') return '';
    return 'alerta';
  }

  imagenRota(id: number): void {
    this.imagenesRotas.update((s) => new Set(s).add(id));
  }

  /** PUT /recompensas/{id}/reclamar (solo si está OBTENIDA) */
  reclamar(r: Recompensa): void {
    const id = r.idRecompensa as number;
    this.reclamando.set(id);
    this.recompensaService.reclamar(id).subscribe({
      next: (actualizada) => {
        this.recompensas.update((lista) =>
          lista.map((x) => (x.idRecompensa === id ? actualizada : x)),
        );
        this.reclamando.set(null);
        this.notificacion.exito(`¡Reclamaste "${r.nombreLogro}"!`);
      },
      error: (err) => {
        this.reclamando.set(null);
        this.notificacion.error(err, 'No se pudo reclamar el logro');
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
