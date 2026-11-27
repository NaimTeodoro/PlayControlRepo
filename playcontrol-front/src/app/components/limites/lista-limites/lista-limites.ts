import { Component, computed, inject, signal } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { forkJoin } from 'rxjs';
import { DIAS_SEMANA } from '../../../models/jugador-joven';
import { ACCIONES_AL_EXCEDER, LimiteJuego, METODOS_BLOQUEO } from '../../../models/limite-juego';
import { ResumenSesiones } from '../../../models/resumen-sesiones';
import { AuthService } from '../../../services/auth-service';
import { claseProgreso, LimiteService } from '../../../services/limite-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { formatoDuracion, SesionService } from '../../../services/sesion-service';
import { ConfirmacionEliminacion } from '../../confirmaciones/confirmacion-eliminacion/confirmacion-eliminacion';

// "Mis límites": límite ACTIVO destacado con su progreso de hoy + tabla con todos los límites
@Component({
  selector: 'app-lista-limites',
  standalone: false,
  templateUrl: './lista-limites.html',
  styleUrl: './lista-limites.css',
})
export class ListaLimites {
  private auth = inject(AuthService);
  private limiteService = inject(LimiteService);
  private sesionService = inject(SesionService);
  private notificacion = inject(NotificacionService);
  private dialog = inject(MatDialog);

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  /** El padre/tutor controla el límite: el jugador solo lo ve */
  readonly controlado = signal(false);
  readonly limites = signal<LimiteJuego[]>([]);
  private resumen = signal<ResumenSesiones | null>(null);
  private idUsuario = this.auth.getIdUsuario();

  readonly columnas = [
    'estado',
    'diario',
    'semanal',
    'mensual',
    'horario',
    'accion',
    'creador',
    'acciones',
  ];
  readonly formatoDuracion = formatoDuracion;
  readonly claseProgreso = claseProgreso;

  readonly activo = computed(() => this.limites().find((l) => l.estado === 'ACTIVO') ?? null);
  // Minutos y % de hoy calculados por el backend frente al límite ACTIVO
  readonly minutosHoy = computed(() => this.resumen()?.minutosHoy ?? 0);
  readonly porcentajeHoy = computed(() => this.resumen()?.porcentajeHoy ?? null);

  // Activo primero
  readonly ordenados = computed(() =>
    [...this.limites()].sort(
      (a, b) => Number(b.estado === 'ACTIVO') - Number(a.estado === 'ACTIVO'),
    ),
  );

  constructor() {
    this.cargar();
  }

  /** "15:00:00" → "15:00" */
  hora(valor?: string | null): string {
    return valor ? valor.slice(0, 5) : '—';
  }

  /** Días permitidos ("LUN,MAR") como etiquetas para los chips */
  dias(limite: LimiteJuego): string[] {
    const codigos = limite.diasPermitidos?.split(',').map((d) => d.trim()) ?? [];
    return DIAS_SEMANA.filter((d) => codigos.includes(d.valor)).map((d) => d.etiqueta);
  }

  metodo(valor?: string | null): string {
    return METODOS_BLOQUEO.find((m) => m.valor === valor)?.etiqueta ?? '—';
  }

  accion(valor?: string | null): string {
    return ACCIONES_AL_EXCEDER.find((a) => a.valor === valor)?.etiqueta ?? '—';
  }

  /** "Configurado por": el propio jugador o su padre/tutor (idCreadoPor) */
  creador(limite: LimiteJuego): string {
    if (limite.idCreadoPor == null || limite.idCreadoPor === this.idUsuario) return 'Tú';
    return 'Tu padre/tutor';
  }

  private cargar(): void {
    const id = this.idUsuario;
    if (id === null) return;
    this.cargando.set(true);

    forkJoin({
      limites: this.limiteService.porUsuario(id),
      resumen: this.sesionService.resumen(id),
      control: this.limiteService.estaControladoPorPadre(),
    }).subscribe({
      next: ({ limites, resumen, control }) => {
        this.controlado.set(control.controlado);
        this.limites.set(limites);
        this.resumen.set(resumen);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  eliminar(limite: LimiteJuego): void {
    if (this.controlado()) return;
    this.dialog
      .open(ConfirmacionEliminacion, {
        data: {
          titulo: '¿Eliminar este límite?',
          mensaje: 'Dejarás de tener este límite de juego. No se puede deshacer.',
        },
      })
      .afterClosed()
      .subscribe((confirmado) => {
        if (!confirmado) return;
        this.limiteService.eliminar(limite.idLimite as number).subscribe({
          next: () => {
            this.notificacion.exito('Límite eliminado');
            this.cargar();
          },
          error: (err) => this.notificacion.error(err),
        });
      });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
