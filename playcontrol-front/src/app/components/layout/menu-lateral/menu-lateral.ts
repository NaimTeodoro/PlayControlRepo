import { Component, computed, inject, output } from '@angular/core';
import { INFO_ROLES, Rol } from '../../../models/rol';
import { AuthService } from '../../../services/auth-service';

interface OpcionMenu {
  etiqueta: string;
  icono: string;
  ruta: string;
}

// Opciones del menú por rol (según la tabla de acceso del contrato de la API)
const OPCIONES: Record<Rol, OpcionMenu[]> = {
  ROLE_JUGADOR: [
    { etiqueta: 'Inicio', icono: 'home', ruta: '/inicio-jugador' },
    { etiqueta: 'Monitoreo', icono: 'query_stats', ruta: '/monitoreo' },
    { etiqueta: 'Mis límites', icono: 'hourglass_bottom', ruta: '/limites' },
    { etiqueta: 'Alertas', icono: 'notifications', ruta: '/alertas' },
    { etiqueta: 'Mis citas', icono: 'event', ruta: '/citas' },
    { etiqueta: 'Dispositivos', icono: 'devices', ruta: '/dispositivos' },
    { etiqueta: 'Logros', icono: 'emoji_events', ruta: '/logros' },
    { etiqueta: 'Ranking', icono: 'leaderboard', ruta: '/ranking' },
    { etiqueta: 'Amigos', icono: 'groups', ruta: '/amigos' },
    { etiqueta: 'Solicitudes', icono: 'link', ruta: '/solicitudes' },
    { etiqueta: 'Recursos', icono: 'menu_book', ruta: '/recursos' },
    { etiqueta: 'Soporte', icono: 'support_agent', ruta: '/soporte' },
    { etiqueta: 'Mi perfil', icono: 'person', ruta: '/perfil-jugador' },
  ],
  ROLE_PADRE: [
    { etiqueta: 'Inicio', icono: 'home', ruta: '/inicio-padre' },
    { etiqueta: 'Mi familia', icono: 'family_restroom', ruta: '/familia' },
    { etiqueta: 'Citas', icono: 'event', ruta: '/citas' },
    { etiqueta: 'Reportes', icono: 'assessment', ruta: '/reportes' },
    { etiqueta: 'Recursos', icono: 'menu_book', ruta: '/recursos' },
    { etiqueta: 'Soporte', icono: 'support_agent', ruta: '/soporte' },
    { etiqueta: 'Mi perfil', icono: 'person', ruta: '/perfil-padre' },
  ],
  ROLE_PROFESIONAL: [
    { etiqueta: 'Inicio', icono: 'home', ruta: '/inicio-profesional' },
    { etiqueta: 'Clientes', icono: 'psychology', ruta: '/clientes' },
    { etiqueta: 'Recursos', icono: 'menu_book', ruta: '/recursos' },
    { etiqueta: 'Mi perfil', icono: 'badge', ruta: '/perfil-profesional' },
  ],
  ROLE_ADMIN: [
    { etiqueta: 'Inicio', icono: 'home', ruta: '/inicio-admin' },
    { etiqueta: 'Usuarios', icono: 'manage_accounts', ruta: '/usuarios' },
    { etiqueta: 'Videojuegos', icono: 'sports_esports', ruta: '/videojuegos' },
    { etiqueta: 'Plataformas', icono: 'devices_other', ruta: '/plataformas' },
    { etiqueta: 'Logros', icono: 'emoji_events', ruta: '/logros-catalogo' },
    { etiqueta: 'Recursos', icono: 'menu_book', ruta: '/recursos' },
  ],
};

@Component({
  selector: 'app-menu-lateral',
  standalone: false,
  templateUrl: './menu-lateral.html',
  styleUrl: './menu-lateral.css',
})
export class MenuLateral {
  private auth = inject(AuthService);

  /** Se emite al elegir una opción (en móvil cierra el menú) */
  readonly navegar = output<void>();

  readonly sesion = this.auth.sesionActual;

  readonly opciones = computed<OpcionMenu[]>(() => {
    const rol = this.sesion()?.roles[0];
    return rol ? OPCIONES[rol] : [];
  });

  // Nombre de GET /auth/me (o el username si no tiene)
  readonly nombre = computed(() => (this.sesion() ? this.auth.getNombreCompleto() : ''));

  readonly etiquetaRol = computed(() => {
    const rol = this.sesion()?.roles[0];
    return rol ? INFO_ROLES[rol].etiqueta : '';
  });

  cerrarSesion(): void {
    this.auth.logout();
  }
}
