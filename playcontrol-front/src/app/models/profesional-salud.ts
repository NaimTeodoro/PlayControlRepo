// ProfesionalSaludDTO — /profesionales-salud (al registrarse queda PENDIENTE con datos "Por completar")

export const ESTADOS_PROFESIONAL = [
  { valor: 'ACTIVO', etiqueta: 'Activo' },
  { valor: 'INACTIVO', etiqueta: 'Inactivo' },
  { valor: 'PENDIENTE', etiqueta: 'Pendiente' },
] as const;
export type EstadoProfesional = (typeof ESTADOS_PROFESIONAL)[number]['valor'];

export interface ProfesionalSalud {
  idProfesional?: number;
  idUsuario: number;
  especialidad: string;
  numeroLicencia: string;
  institucion?: string | null;
  aniosExperiencia?: number | null;
  areaTrabajo?: string | null;
  certificaciones?: string | null;
  telefonoConsulta?: string | null;
  horarioConsulta?: string | null;
  verificadoPor?: string | null;
  fechaVerificacion?: string | null;
  estado?: EstadoProfesional;
}

/** El registro crea el perfil con "Por completar" y licencia "PENDIENTE-<id>": aún sin datos reales */
export function perfilIncompleto(p: ProfesionalSalud): boolean {
  return (
    !p.especialidad ||
    p.especialidad === 'Por completar' ||
    !p.numeroLicencia ||
    p.numeroLicencia.startsWith('PENDIENTE-')
  );
}
