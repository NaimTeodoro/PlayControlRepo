// DispositivoDTO — /dispositivos

export const TIPOS_DISPOSITIVO = [
  { valor: 'PC', etiqueta: 'PC', icono: 'computer' },
  { valor: 'CONSOLA', etiqueta: 'Consola', icono: 'sports_esports' },
  { valor: 'MOVIL', etiqueta: 'Móvil', icono: 'smartphone' },
] as const;
export type TipoDispositivo = (typeof TIPOS_DISPOSITIVO)[number]['valor'];

export const ESTADOS_ACTIVO = [
  { valor: 'ACTIVO', etiqueta: 'Activo' },
  { valor: 'INACTIVO', etiqueta: 'Inactivo' },
] as const;
export type EstadoActivo = (typeof ESTADOS_ACTIVO)[number]['valor'];

export interface Dispositivo {
  idDispositivo?: number;
  idUsuario: number;
  tipoDispositivo?: TipoDispositivo;
  modelo?: string | null;
  sistemaOperativo?: string | null;
  identificadorDisp?: string | null;
  fechaAsociacion?: string | null;
  estado?: EstadoActivo;
  ultimaActividad?: string | null;
  versionApp?: string | null;
}
