// PadreHijoDTO — /padre-hijo (PENDIENTE hasta que el hijo autoriza → ACTIVO)

export const ESTADOS_VINCULO = [
  { valor: 'PENDIENTE', etiqueta: 'Pendiente' },
  { valor: 'ACTIVO', etiqueta: 'Activo' },
  { valor: 'INACTIVO', etiqueta: 'Inactivo' },
] as const;
export type EstadoVinculo = (typeof ESTADOS_VINCULO)[number]['valor'];

export const TIPOS_RELACION = [
  { valor: 'PADRE', etiqueta: 'Padre' },
  { valor: 'MADRE', etiqueta: 'Madre' },
  { valor: 'TUTOR', etiqueta: 'Tutor/a' },
] as const;
export type TipoRelacion = (typeof TIPOS_RELACION)[number]['valor'];

export interface PadreHijo {
  idPadreHijo?: number;
  idPadreTutor: number;
  idJugadorJoven: number;
  tipoRelacion?: TipoRelacion | null;
  fechaAsociacion?: string | null;
  estado?: EstadoVinculo;
  permisosSupervision?: string | null; // "VER_REPORTES,CONFIGURAR_LIMITES"
  autorizado?: boolean;
  // Solo lectura: datos del hijo
  idUsuarioHijo?: number;
  usernameHijo?: string;
  nombreHijo?: string;
  apellidoHijo?: string;
  codigoVinculacionHijo?: string;
}

/** Body de POST /padre-hijo/vincular */
export interface Vincular {
  idPadreTutor: number;
  codigoVinculacion: string; // PC-XXXXXX
  tipoRelacion?: TipoRelacion;
}
