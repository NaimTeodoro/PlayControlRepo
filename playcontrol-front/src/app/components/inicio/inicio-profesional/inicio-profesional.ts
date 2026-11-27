import { Component, computed, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ProfesionalCliente } from '../../../models/profesional-cliente';
import { perfilIncompleto, ProfesionalSalud } from '../../../models/profesional-salud';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import {
  nombreCliente,
  ProfesionalClienteService,
} from '../../../services/profesional-cliente-service';
import { ProfesionalService } from '../../../services/profesional-service';
import { isoLocal } from '../../../services/sesion-service';

// Inicio del profesional: aviso si el perfil está PENDIENTE, cantidad de clientes y próximas consultas
@Component({
  selector: 'app-inicio-profesional',
  standalone: false,
  templateUrl: './inicio-profesional.html',
  styleUrl: './inicio-profesional.css',
})
export class InicioProfesional {
  private auth = inject(AuthService);
  private notificacion = inject(NotificacionService);

  readonly sesion = this.auth.sesionActual;
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly perfil = signal<ProfesionalSalud | null>(null);
  readonly clientes = signal<ProfesionalCliente[]>([]);
  readonly nombreCliente = nombreCliente;

  readonly perfilPendiente = computed(() => this.perfil()?.estado === 'PENDIENTE');
  // Datos aún sin completar (marcadores del registro) vs. completos pero pendientes de verificación
  readonly perfilSinDatos = computed(() => {
    const p = this.perfil();
    return !!p && perfilIncompleto(p);
  });
  readonly activos = computed(() => this.clientes().filter((c) => c.estado === 'ACTIVO').length);

  // Próximas consultas: desde ahora en adelante, la más cercana primero
  readonly proximas = computed(() => {
    const ahora = isoLocal();
    return this.clientes()
      .filter((c) => c.proximaConsulta && c.proximaConsulta.slice(0, 19) >= ahora)
      .sort((a, b) => (a.proximaConsulta as string).localeCompare(b.proximaConsulta as string));
  });

  readonly consultasSemana = computed(() => {
    const limite = isoLocal(new Date(Date.now() + 7 * 86400000));
    return this.proximas().filter((c) => (c.proximaConsulta as string).slice(0, 19) <= limite)
      .length;
  });

  constructor() {
    const id = this.auth.getIdUsuario();
    const idProfesional = this.auth.getIdProfesional();
    if (id === null || idProfesional === null) {
      this.cargando.set(false);
      return;
    }
    forkJoin({
      perfil: inject(ProfesionalService).porUsuario(id),
      clientes: inject(ProfesionalClienteService).porProfesional(idProfesional),
    }).subscribe({
      next: ({ perfil, clientes }) => {
        this.perfil.set(perfil);
        this.clientes.set(clientes);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
