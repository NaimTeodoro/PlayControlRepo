// LogroDTO — /logros (catálogo; escritura solo ADMIN)

export const TIPOS_LOGRO = [
  { valor: 'DIARIO', etiqueta: 'Diario' },
  { valor: 'SEMANAL', etiqueta: 'Semanal' },
  { valor: 'HITO', etiqueta: 'Hito' },
] as const;
export type TipoLogro = (typeof TIPOS_LOGRO)[number]['valor'];

export const CONDICIONES_LOGRO = [
  { valor: 'DIAS_RESPETANDO_LIMITE', etiqueta: 'Días respetando el límite' },
  { valor: 'PAUSAS_REALIZADAS', etiqueta: 'Pausas realizadas' },
  { valor: 'DIAS_SIN_JUEGO', etiqueta: 'Días sin jugar' },
  { valor: 'SESIONES_CORTAS', etiqueta: 'Sesiones cortas' },
  { valor: 'RECURSOS_COMPLETADOS', etiqueta: 'Recursos completados' },
] as const;
export type CondicionLogro = (typeof CONDICIONES_LOGRO)[number]['valor'];

export interface Logro {
  idLogro?: number;
  nombreLogro: string;
  descripcion?: string | null;
  puntosAsignados: number;
  tipo?: TipoLogro | null;
  condicion?: string | null;
  condicionTipo?: CondicionLogro | null;
  valorCondicion?: number | null;
  imagenUrl?: string | null;
  activo?: boolean;
}
