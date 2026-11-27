import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { catchError, forkJoin, of } from 'rxjs';
import { AlertaJuego } from '../../../models/alerta-juego';
import { ProfesionalCliente } from '../../../models/profesional-cliente';
import { ResumenSesiones } from '../../../models/resumen-sesiones';
import { SesionJuego } from '../../../models/sesion-juego';
import {
  AlertaService,
  esPendiente,
  infoTipoAlerta,
  ordenarAlertas,
} from '../../../services/alerta-service';
import { claseProgreso } from '../../../services/limite-service';
import { formatoDuracion, minutosSesion, SesionService } from '../../../services/sesion-service';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import {
  nombreCliente,
  ProfesionalClienteService,
} from '../../../services/profesional-cliente-service';
import { ESTADOS_PROFESIONAL } from '../../../models/profesional-salud';

// Ficha de solo lectura de un cliente: diagnóstico, plan, frecuencia, próxima consulta y notas
@Component({
  selector: 'app-detalle-cliente',
  standalone: false,
  templateUrl: './detalle-cliente.html',
  styleUrl: './detalle-cliente.css',
})
export class DetalleCliente {
  private notificacion = inject(NotificacionService);

  readonly cliente = signal<ProfesionalCliente | null>(null);
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly nombreCliente = nombreCliente;
  readonly formatoDuracion = formatoDuracion;
  readonly minutosSesion = minutosSesion;
  readonly claseProgreso = claseProgreso;
  readonly infoTipoAlerta = infoTipoAlerta;
  readonly esPendiente = esPendiente;

  // Datos de juego del jugador (el profesional puede leerlos con idUsuarioJugador)
  readonly cargandoJuego = signal(false);
  readonly errorJuego = signal(false);
  readonly resumen = signal<ResumenSesiones | null>(null);
  readonly sesiones = signal<SesionJuego[]>([]);
  readonly alertas = signal<AlertaJuego[]>([]);
  private sesionService = inject(SesionService);
  private alertaService = inject(AlertaService);

  constructor() {
    const id = Number(inject(ActivatedRoute).snapshot.paramMap.get('id'));
    const idProfesional = inject(AuthService).getIdProfesional();
    if (idProfesional === null) {
      this.cargando.set(false);
      return;
    }

    // Se busca entre los clientes del profesional logueado (solo puede ver los suyos)
    inject(ProfesionalClienteService)
      .porProfesional(idProfesional)
      .subscribe({
        next: (clientes) => {
          const c = clientes.find((x) => x.idProfesionalCliente === id) ?? null;
          this.cliente.set(c);
          this.cargando.set(false);
          if (c?.idUsuarioJugador) this.cargarJuego(c.idUsuarioJugador);
        },
        error: (err) => {
          this.cargando.set(false);
          this.errorCarga.set(true);
          this.notificacion.error(err, 'No se encontró el cliente');
        },
      });
  }

  /** Resumen de tiempo, últimas sesiones y alertas del jugador */
  private cargarJuego(idUsuario: number): void {
    this.cargandoJuego.set(true);
    forkJoin({
      resumen: this.sesionService.resumen(idUsuario),
      sesiones: this.sesionService.porUsuario(idUsuario),
      alertas: this.alertaService.porUsuario(idUsuario),
    })
      .pipe(
        catchError(() => {
          this.errorJuego.set(true);
          return of(null);
        }),
      )
      .subscribe((datos) => {
        if (datos) {
          this.resumen.set(datos.resumen);
          this.sesiones.set(datos.sesiones.slice(0, 8)); // ya vienen las más recientes primero
          this.alertas.set(ordenarAlertas(datos.alertas).slice(0, 8));
        }
        this.cargandoJuego.set(false);
      });
  }

  pendientes(): number {
    return this.alertas().filter(esPendiente).length;
  }

  estado(valor?: string): string {
    return ESTADOS_PROFESIONAL.find((e) => e.valor === valor)?.etiqueta ?? '—';
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
