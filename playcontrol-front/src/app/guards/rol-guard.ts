import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Rol } from '../models/rol';
import { AuthService } from '../services/auth-service';

/** Lee data: { roles: [...] } de la ruta; si el usuario no tiene el rol, lo manda a su inicio */
export const rolGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.estaLogueado()) {
    return router.createUrlTree(['/login']);
  }

  const permitidos = (route.data['roles'] as Rol[] | undefined) ?? [];
  if (permitidos.length === 0 || permitidos.some((rol) => auth.tieneRol(rol))) {
    return true;
  }
  return router.createUrlTree([auth.rutaInicio()]);
};
