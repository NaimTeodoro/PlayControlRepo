// PadreTutorDTO — /padres-tutores (idPadreTutor es distinto de idUsuario)

export const FRECUENCIAS_REVISION = [
  { valor: 'DIARIA', etiqueta: 'Diaria' },
  { valor: 'SEMANAL', etiqueta: 'Semanal' },
  { valor: 'MENSUAL', etiqueta: 'Mensual' },
] as const;
export type FrecuenciaRevision = (typeof FRECUENCIAS_REVISION)[number]['valor'];

export interface PadreTutor {
  idPadreTutor?: number;
  idUsuario: number;
  metodosControlActual?: string | null;
  preocupacionesPrincipales?: string | null;
  objetivosSupervision?: string | null;
  frecuenciaRevisionReportes?: FrecuenciaRevision | null;
  configuracionAlertas?: string | null; // JSON en texto
  fechaRegistro?: string | null;
}
