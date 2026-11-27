import { Intensidad, TipoAlerta } from './alerta-juego';

// HistorialAlertaDTO — /historial-alertas

export const ESTADOS_HISTORIAL = [
  { valor: 'ABIERTO', etiqueta: 'Abierto' },
  { valor: 'CERRADO', etiqueta: 'Cerrado' },
] as const;
export type EstadoHistorial = (typeof ESTADOS_HISTORIAL)[number]['valor'];

// Texto libre en el backend; estos son los que usan los datos
export const ACCIONES_HISTORIAL = [
  { valor: 'TERMINO_SESION', etiqueta: 'Terminó la sesión' },
  { valor: 'PAUSA', etiqueta: 'Hizo una pausa' },
  { valor: 'IGNORADA', etiqueta: 'Ignorada' },
] as const;

export interface HistorialAlerta {
  idHistorial?: number;
  idAlerta: number;
  descripcion?: string | null;
  accionRealizada?: string | null;
  impactoEnSesion?: string | null;
  efectividadAlerta?: Intensidad | null;
  fecha?: string;
  estado?: EstadoHistorial;
  notasAdicionales?: string | null;
  // Solo lectura
  idUsuario?: number;
  tipoAlerta?: TipoAlerta;
  respuestaUsuario?: string | null;
}
