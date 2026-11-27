import { Component, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { AuthService } from '../../../services/auth-service';
import { NotificacionService } from '../../../services/notificacion-service';
import { LogroService } from '../../../services/logro-service';
import { PlataformaService } from '../../../services/plataforma-service';
import { RecursoService } from '../../../services/recurso-service';
import { UsuarioService } from '../../../services/usuario-service';
import { VideojuegoService } from '../../../services/videojuego-service';

interface Acceso {
  titulo: string;
  descripcion: string;
  icono: string;
  ruta: string;
  total: number | null;
}

// Inicio del administrador: totales y accesos directos a usuarios, videojuegos, plataformas, logros y recursos
@Component({
  selector: 'app-inicio-admin',
  standalone: false,
  templateUrl: './inicio-admin.html',
  styleUrl: './inicio-admin.css',
})
export class InicioAdmin {
  private notificacion = inject(NotificacionService);

  readonly sesion = inject(AuthService).sesionActual;
  readonly cargando = signal(true);
  readonly errorCarga = signal(false);
  readonly accesos = signal<Acceso[]>([
    {
      titulo: 'Usuarios',
      descripcion: 'Crea, edita y elimina cuentas de usuario.',
      icono: 'manage_accounts',
      ruta: '/usuarios',
      total: null,
    },
    {
      titulo: 'Videojuegos',
      descripcion: 'Administra el catálogo de videojuegos.',
      icono: 'sports_esports',
      ruta: '/videojuegos',
      total: null,
    },
    {
      titulo: 'Plataformas',
      descripcion: 'Gestiona PC, consolas y móviles.',
      icono: 'devices_other',
      ruta: '/plataformas',
      total: null,
    },
    {
      titulo: 'Catálogo de logros',
      descripcion: 'Metas y puntos que ganan los jugadores.',
      icono: 'emoji_events',
      ruta: '/logros-catalogo',
      total: null,
    },
    {
      titulo: 'Recursos educativos',
      descripcion: 'Artículos, videos, guías y preguntas frecuentes.',
      icono: 'menu_book',
      ruta: '/recursos',
      total: null,
    },
  ]);

  constructor() {
    forkJoin({
      usuarios: inject(UsuarioService).listar(),
      videojuegos: inject(VideojuegoService).listar(),
      plataformas: inject(PlataformaService).listar(),
      logros: inject(LogroService).listar(),
      recursos: inject(RecursoService).listar(),
    }).subscribe({
      next: (d) => {
        const totales = [
          d.usuarios.length,
          d.videojuegos.length,
          d.plataformas.length,
          d.logros.length,
          d.recursos.length,
        ];
        this.accesos.update((lista) => lista.map((a, i) => ({ ...a, total: totales[i] })));
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
