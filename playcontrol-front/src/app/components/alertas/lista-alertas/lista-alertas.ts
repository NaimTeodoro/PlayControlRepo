import { Component, computed, effect, inject, signal, viewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AlertaJuego, TIPOS_ALERTA } from '../../../models/alerta-juego';
import {
  AlertaService,
  claseIntensidad,
  esPendiente,
  infoTipoAlerta,
  ordenarAlertas,
} from '../../../services/alerta-service';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';

type Filtro = 'todas' | 'pendientes' | 'respondidas';

// Lista de alertas del jugador: pendientes primero, filtros por estado y tipo, y respuesta rápida
// (PUT /alertas-juego/{id}/responder)
@Component({
  selector: 'app-lista-alertas',
  standalone: false,
  templateUrl: './lista-alertas.html',
  styleUrl: './lista-alertas.css',
})
export class ListaAlertas {
  private auth = inject(AuthService);
  private alertaService = inject(AlertaService);
  private notificacion = inject(NotificacionService);

  readonly cargando = signal(true);

  readonly errorCarga = signal(false);
  readonly alertas = signal<AlertaJuego[]>([]);
  readonly filtro = signal<Filtro>('todas');
  readonly filtroTipo = signal<string>('');
  readonly tipos = TIPOS_ALERTA;
  readonly respondiendo = signal<number | null>(null);

  readonly columnas = ['tipo', 'mensaje', 'intensidad', 'fecha', 'estado', 'acciones'];
  readonly datos = new MatTableDataSource<AlertaJuego>([]);
  private paginador = viewChild(MatPaginator);

  readonly esPendiente = esPendiente;
  readonly infoTipo = infoTipoAlerta;
  readonly claseIntensidad = claseIntensidad;

  readonly totalPendientes = computed(() => this.alertas().filter(esPendiente).length);

  readonly filtradas = computed(() => {
    const tipo = this.filtroTipo();
    const todas = ordenarAlertas(this.alertas()).filter((a) => !tipo || a.tipoAlerta === tipo);
    switch (this.filtro()) {
      case 'pendientes':
        return todas.filter(esPendiente);
      case 'respondidas':
        return todas.filter((a) => !esPendiente(a));
      default:
        return todas;
    }
  });

  constructor() {
    effect(() => (this.datos.data = this.filtradas()));
    effect(() => {
      const paginador = this.paginador();
      if (paginador) this.datos.paginator = paginador;
    });
    this.cargar();
  }

  private cargar(): void {
    const id = this.auth.getIdUsuario();
    if (id === null) return;
    this.cargando.set(true);
    this.alertaService.porUsuario(id).subscribe({
      next: (alertas) => {
        this.alertas.set(alertas);
        this.cargando.set(false);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorCarga.set(true);
        this.notificacion.error(err);
      },
    });
  }

  responder(alerta: AlertaJuego): void {
    this.respondiendo.set(alerta.idAlerta ?? null);
    this.alertaService.responder(alerta.idAlerta as number, 'Entendido').subscribe({
      next: (actualizada) => {
        this.alertas.update((lista) =>
          lista.map((a) => (a.idAlerta === actualizada.idAlerta ? actualizada : a)),
        );
        this.respondiendo.set(null);
        this.notificacion.exito('Alerta marcada como respondida');
      },
      error: (err) => {
        this.respondiendo.set(null);
        // 409: ya estaba respondida
        this.notificacion.error(err, 'No se pudo responder la alerta');
      },
    });
  }

  /** Reintenta la carga recargando la página */
  recargar(): void {
    location.reload();
  }
}
