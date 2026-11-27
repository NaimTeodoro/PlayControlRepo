import { Component, computed, inject, signal } from '@angular/core';
import { Cita } from '../../../models/cita';
import { AuthService } from '../../../services/auth-service';
import { CitaService, nombreJugadorCita, proximaCita } from '../../../services/cita-service';
import { NotificacionService } from '../../../services/notificacion-service';

interface GrupoCitas {
  idJugadorJoven: number;
  nombre: string;
  citas: Cita[];
}

// "Mis citas" (solo lectura): el jugador ve sus consultas; el padre, las de todos sus hijos
// con vínculo activo, agrupadas por hijo (GET /citas/mias)
@Component({
  selector: 'app-lista-citas',
  standalone: false,
  templateUrl: './lista-citas.html',
  styleUrl: './lista-citas.css',
})
export class ListaCitas {
  private citaService = inject(CitaService);
  private notificacion = inject(NotificacionService);

  readonly esPadre = inject(AuthService).tieneRol('ROLE_PADRE');

  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly citas = signal<Cita[]>([]);
  /** Filtro del padre: 'todos' o el idJugadorJoven del hijo */
  readonly filtro = signal<'todos' | number>('todos');

  /** La cita más próxima (se resalta) */
  readonly destacada = computed(() => proximaCita(this.citas()));

  /** Grupos por hijo (en el orden en que llegan: la más próxima primero) */
  readonly grupos = computed<GrupoCitas[]>(() => {
    const mapa = new Map<number, GrupoCitas>();
    for (const c of this.citas()) {
      const g = mapa.get(c.idJugadorJoven);
      if (g) g.citas.push(c);
      else mapa.set(c.idJugadorJoven, { idJugadorJoven: c.idJugadorJoven, nombre: nombreJugadorCita(c), citas: [c] });
    }
    return [...mapa.values()];
  });

  readonly gruposVisibles = computed(() => {
    const f = this.filtro();
    return f === 'todos' ? this.grupos() : this.grupos().filter((g) => g.idJugadorJoven === f);
  });

  constructor() {
    this.cargar();
  }

  esDestacada(c: Cita): boolean {
    return this.destacada()?.idProfesionalCliente === c.idProfesionalCliente;
  }

  cargar(): void {
    this.cargando.set(true);
    this.errorCarga.set(false);
    this.citaService.mias().subscribe({
      next: (citas) => {
        this.citas.set(citas);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }
}
