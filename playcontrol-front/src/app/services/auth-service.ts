import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, switchMap, tap, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { RegistroRespuesta } from '../models/registro-respuesta';
import { RegistroUsuario } from '../models/registro-usuario';
import { INFO_ROLES, Rol } from '../models/rol';
import { Token } from '../models/token';
import { UsuarioActual } from '../models/usuario-actual';

// Datos de la sesión guardados en localStorage: login + GET /auth/me
export interface SesionActual {
  idUsuario: number;
  username: string;
  roles: Rol[];
  nombre: string | null;
  apellido: string | null;
  idJugadorJoven: number | null;
  idPadreTutor: number | null;
  idProfesional: number | null;
}

const CLAVES = {
  token: 'token',
  idUsuario: 'idUsuario',
  username: 'username',
  roles: 'roles',
  perfil: 'perfil', // { nombre, apellido, idJugadorJoven, idPadreTutor, idProfesional }
};

type Perfil = Pick<
  SesionActual,
  'nombre' | 'apellido' | 'idJugadorJoven' | 'idPadreTutor' | 'idProfesional'
>;

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private url = `${environment.apiUrl}/auth`;

  // Estado reactivo de la sesión (para menú, barra y layout)
  private sesion = signal<SesionActual | null>(this.leerSesion());
  readonly sesionActual = this.sesion.asReadonly();
  readonly logueado = computed(() => this.sesion() !== null);

  /** POST /auth/login y enseguida GET /auth/me (ids de perfil, nombre y apellido) */
  login(username: string, password: string): Observable<UsuarioActual> {
    return this.http.post<Token>(`${this.url}/login`, { username, password }).pipe(
      tap((token) => localStorage.setItem(CLAVES.token, token.jwtToken)),
      switchMap(() => this.http.get<UsuarioActual>(`${this.url}/me`)),
      tap((yo) => this.guardarSesion(yo)),
      catchError((err) => {
        this.logout(false); // sin /me no hay sesión completa
        return throwError(() => err);
      }),
    );
  }

  registrar(datos: RegistroUsuario): Observable<RegistroRespuesta> {
    return this.http.post<RegistroRespuesta>(`${this.url}/register`, datos);
  }

  /** Borra la sesión y (por defecto) vuelve al login */
  logout(redirigir = true): void {
    Object.values(CLAVES).forEach((clave) => localStorage.removeItem(clave));
    this.sesion.set(null);
    if (redirigir) {
      this.router.navigate(['/login']);
    }
  }

  getToken(): string | null {
    return localStorage.getItem(CLAVES.token);
  }

  getIdUsuario(): number | null {
    return this.sesion()?.idUsuario ?? null;
  }

  getIdJugadorJoven(): number | null {
    return this.sesion()?.idJugadorJoven ?? null;
  }

  getIdPadreTutor(): number | null {
    return this.sesion()?.idPadreTutor ?? null;
  }

  getIdProfesional(): number | null {
    return this.sesion()?.idProfesional ?? null;
  }

  getUsername(): string | null {
    return this.sesion()?.username ?? null;
  }

  /** "Nombre Apellido" o, si faltan, el username */
  getNombreCompleto(): string {
    const s = this.sesion();
    const nombre = [s?.nombre, s?.apellido].filter((p) => !!p).join(' ');
    return nombre || s?.username || '';
  }

  getRoles(): Rol[] {
    return this.sesion()?.roles ?? [];
  }

  /** Rol principal del usuario (el backend asigna uno por usuario) */
  getRol(): Rol | null {
    return this.getRoles()[0] ?? null;
  }

  tieneRol(rol: Rol): boolean {
    return this.getRoles().includes(rol);
  }

  /** Hay token y no ha expirado (vale 10 h). Si expiró, limpia la sesión. */
  estaLogueado(): boolean {
    const token = this.getToken();
    if (!token || this.sesion() === null) {
      return false;
    }
    if (this.tokenExpirado(token)) {
      this.logout(false);
      return false;
    }
    return true;
  }

  /** Ruta de inicio según el rol */
  rutaInicio(): string {
    const rol = this.getRol();
    return rol ? INFO_ROLES[rol].inicio : '/login';
  }

  private guardarSesion(yo: UsuarioActual): void {
    const perfil: Perfil = {
      nombre: yo.nombre,
      apellido: yo.apellido,
      idJugadorJoven: yo.idJugadorJoven,
      idPadreTutor: yo.idPadreTutor,
      idProfesional: yo.idProfesional,
    };
    localStorage.setItem(CLAVES.idUsuario, String(yo.idUsuario));
    localStorage.setItem(CLAVES.username, yo.username);
    localStorage.setItem(CLAVES.roles, JSON.stringify(yo.roles));
    localStorage.setItem(CLAVES.perfil, JSON.stringify(perfil));
    this.sesion.set({ idUsuario: yo.idUsuario, username: yo.username, roles: yo.roles, ...perfil });
  }

  /** Sesión guardada; una sesión antigua sin datos de /auth/me obliga a entrar de nuevo */
  private leerSesion(): SesionActual | null {
    const id = localStorage.getItem(CLAVES.idUsuario);
    const username = localStorage.getItem(CLAVES.username);
    const roles = localStorage.getItem(CLAVES.roles);
    const perfil = localStorage.getItem(CLAVES.perfil);
    if (!localStorage.getItem(CLAVES.token) || !id || !username || !roles || !perfil) {
      return null;
    }
    try {
      return {
        idUsuario: Number(id),
        username,
        roles: JSON.parse(roles) as Rol[],
        ...(JSON.parse(perfil) as Perfil),
      };
    } catch {
      return null;
    }
  }

  /** Lee el campo exp (segundos) del payload del JWT */
  private tokenExpirado(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
      return typeof payload.exp === 'number' && payload.exp * 1000 < Date.now();
    } catch {
      return false; // Si no se puede leer, que decida el backend
    }
  }
}
