import {
  HttpBackend,
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from '../services/auth-service';
import { NotificacionService } from '../services/notificacion-service';

// Agrega "Authorization: Bearer <token>" a todas las peticiones excepto login y registro.
// Si la sesión ya no es válida, cierra sesión y vuelve al login.
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private auth = inject(AuthService);
  private notificacion = inject(NotificacionService);
  // Cliente HTTP que NO pasa por los interceptores (evita dependencia circular y recursión)
  private httpDirecto = new HttpClient(inject(HttpBackend));
  private ultimaVerificacion = 0;

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const esPublica = req.url.includes('/auth/login') || req.url.includes('/auth/register');
    const token = this.auth.getToken();

    const peticion =
      !esPublica && token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next.handle(peticion).pipe(
      catchError((err: HttpErrorResponse) => {
        if (!esPublica && token) {
          if (err.status === 401) {
            this.cerrarSesion();
          } else if (err.status === 403 && !(err.error as { detail?: string } | null)?.detail) {
            // Los 403 de regla de negocio traen `detail`: no son de sesión, no hay que verificar
            this.verificarSesion(token);
          }
        }
        return throwError(() => err);
      }),
    );
  }

  /**
   * Solo se llama con 403 sin cuerpo. El backend responde 403 (no 401) tanto si el token es inválido como si falta el rol, y sin
   * cuerpo que los distinga. Para saberlo se consulta /plataformas, que puede leer cualquier
   * usuario autenticado: si también da 401/403, el token no sirve y se cierra la sesión.
   * Como máximo una verificación cada 30 s.
   */
  private verificarSesion(token: string): void {
    const ahora = Date.now();
    if (ahora - this.ultimaVerificacion < 30_000) return;
    this.ultimaVerificacion = ahora;

    this.httpDirecto
      .get(`${environment.apiUrl}/plataformas`, { headers: { Authorization: `Bearer ${token}` } })
      .subscribe({
        error: (e: HttpErrorResponse) => {
          // Solo si sigue siendo la misma sesión (el usuario no cambió de cuenta mientras tanto)
          if ((e.status === 401 || e.status === 403) && this.auth.getToken() === token) {
            this.cerrarSesion();
          }
        },
      });
  }

  private cerrarSesion(): void {
    this.auth.logout();
    this.notificacion.error('Tu sesión expiró. Vuelve a iniciar sesión.');
  }
}
