import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { AuthService } from '../services/auth-service';
import { LimiteService } from '../services/limite-service';
import { NotificacionService } from '../services/notificacion-service';

/**
 * Para el JUGADOR: si su padre/tutor controla el límite, no puede crear ni editar límites.
 * Padre y admin pasan sin comprobación.
 */
export const limiteControlGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  if (!auth.tieneRol('ROLE_JUGADOR')) return true;

  const router = inject(Router);
  const notificacion = inject(NotificacionService);
  return inject(LimiteService)
    .estaControladoPorPadre()
    .pipe(
      map(({ controlado }) => {
        if (!controlado) return true;
        notificacion.error('Tu límite está controlado por tu padre/tutor');
        return router.createUrlTree(['/limites']);
      }),
    );
};
