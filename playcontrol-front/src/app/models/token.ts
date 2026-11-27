import { Rol } from './rol';

// Respuesta de POST /auth/login
export interface Token {
  jwtToken: string;
  idUsuario: number;
  username: string;
  roles: Rol[];
}
