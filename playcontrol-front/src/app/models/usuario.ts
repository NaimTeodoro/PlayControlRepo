import { Rol } from './rol';

// UsuarioDTO — /usuarios (solo ADMIN). password solo se envía (obligatorio al crear) y nunca se devuelve.

export const ESTADOS_USUARIO = [
  { valor: 'ACTIVO', etiqueta: 'Activo' },
  { valor: 'INACTIVO', etiqueta: 'Inactivo' },
] as const;
export type EstadoUsuario = (typeof ESTADOS_USUARIO)[number]['valor'];

export const NIVELES_TECNOLOGIA = [
  { valor: 'BASICO', etiqueta: 'Básico' },
  { valor: 'MEDIO', etiqueta: 'Medio' },
  { valor: 'ALTO', etiqueta: 'Alto' },
] as const;
export type NivelTecnologia = (typeof NIVELES_TECNOLOGIA)[number]['valor'];

export interface Usuario {
  idUsuario?: number;
  username: string;
  password?: string;
  enabled?: boolean;
  nombre?: string | null;
  apellido?: string | null;
  email?: string | null;
  telefono?: string | null;
  fechaNacimiento?: string | null; // yyyy-MM-dd
  genero?: string | null;
  estado?: EstadoUsuario | null;
  fechaRegistro?: string | null;
  ultimoAcceso?: string | null;
  pais?: string | null;
  ciudad?: string | null;
  direccion?: string | null;
  nivelTecnologia?: NivelTecnologia | null;
  configuracionPrivacidad?: string | null;
  roles?: Rol[];
}
