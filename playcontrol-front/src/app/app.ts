import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, ViewEncapsulation, computed, inject, viewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { toSignal } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router } from '@angular/router';
import { filter, map } from 'rxjs';
import { AuthService } from './services/auth-service';

// Rutas que se muestran sin barra ni menú lateral
const RUTAS_PUBLICAS = ['/login', '/registro'];

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.html',
  styleUrl: './app.css',
  // Sin encapsulación: app.css define las variables globales de color (:root)
  encapsulation: ViewEncapsulation.None,
})
export class App {
  private auth = inject(AuthService);
  private router = inject(Router);

  // URL actual (para ocultar el layout en login/registro)
  private url = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      map((e) => e.urlAfterRedirects),
    ),
  );

  // Por debajo de 768px el menú pasa a modo "over" (hamburguesa)
  readonly esMovil = toSignal(
    inject(BreakpointObserver)
      .observe('(max-width: 767.98px)')
      .pipe(map((r) => r.matches)),
    { initialValue: false },
  );

  readonly mostrarLayout = computed(() => {
    const url = this.url();
    return this.auth.logueado() && !!url && !RUTAS_PUBLICAS.some((ruta) => url.startsWith(ruta));
  });

  // El sidenav vive dentro de un @if, por eso se obtiene con viewChild
  private sidenav = viewChild(MatSidenav);

  alternarMenu(): void {
    this.sidenav()?.toggle();
  }

  /** En móvil (modo "over") el menú se cierra al elegir una opción */
  cerrarMenuEnMovil(): void {
    if (this.esMovil()) this.sidenav()?.close();
  }
}
