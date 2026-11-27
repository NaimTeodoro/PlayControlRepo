import { Rol } from './rol';

// UsuarioActualDTO — GET /auth/me (los id* de perfil son null si no aplican al rol)
export interface UsuarioActual {
  idUsuario: number;
  username: string;
  nombre: string | null;
  apellido: string | null;
  email: string | null;
  roles: Rol[];
  idJugadorJoven: number | null;
  idPadreTutor: number | null;
  idProfesional: number | null;
}
