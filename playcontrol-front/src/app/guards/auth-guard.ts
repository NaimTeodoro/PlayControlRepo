import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';

/** Solo deja pasar a usuarios con sesión iniciada */
export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  return auth.estaLogueado() ? true : inject(Router).createUrlTree(['/login']);
};

/** Para login/registro: si ya hay sesión, manda al inicio de su rol */
export const invitadoGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  return auth.estaLogueado() ? inject(Router).createUrlTree([auth.rutaInicio()]) : true;
};
