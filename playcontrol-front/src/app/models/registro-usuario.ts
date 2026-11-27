import { Rol } from './rol';

// Body de POST /auth/register (password mínimo 6; ROLE_ADMIN prohibido)
export interface RegistroUsuario {
  username: string;
  password: string;
  email: string;
  nombre: string;
  apellido: string;
  rol: Exclude<Rol, 'ROLE_ADMIN'>;
}
