import { Component, computed, inject, input, output } from '@angular/core';
import { INFO_ROLES } from '../../../models/rol';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-barra-navegacion',
  standalone: false,
  templateUrl: './barra-navegacion.html',
  styleUrl: './barra-navegacion.css',
})
export class BarraNavegacion {
  private auth = inject(AuthService);

  /** En pantallas pequeñas se muestra el botón hamburguesa */
  readonly mostrarBotonMenu = input(false);
  readonly alternarMenu = output<void>();

  readonly sesion = this.auth.sesionActual;
  readonly hoy = new Date();
  // Nombre de pila (GET /auth/me) para el saludo; si no hay, el username
  readonly nombrePila = computed(
    () => this.sesion()?.nombre?.split(' ')[0] || this.sesion()?.username || '',
  );

  readonly infoRol = computed(() => {
    const rol = this.sesion()?.roles[0];
    return rol ? INFO_ROLES[rol] : null;
  });

  cerrarSesion(): void {
    this.auth.logout();
  }
}
