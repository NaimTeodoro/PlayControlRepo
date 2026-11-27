// SesionJuegoDTO — /sesiones (más recientes primero en /sesiones/usuario/{id})

export const ESTADOS_SESION = [
  { valor: 'EN_CURSO', etiqueta: 'En curso' },
  { valor: 'FINALIZADA', etiqueta: 'Finalizada' },
  { valor: 'INTERRUMPIDA', etiqueta: 'Interrumpida' },
] as const;
export type EstadoSesion = (typeof ESTADOS_SESION)[number]['valor'];

export const NIVELES_ENERGIA = [
  { valor: 'BAJA', etiqueta: 'Baja', icono: 'battery_1_bar' },
  { valor: 'NORMAL', etiqueta: 'Normal', icono: 'battery_4_bar' },
  { valor: 'ALTA', etiqueta: 'Alta', icono: 'battery_full' },
] as const;
export type EnergiaFisica = (typeof NIVELES_ENERGIA)[number]['valor'];

export const ESTADOS_ANIMO = [
  { valor: 'BIEN', etiqueta: 'Bien', icono: 'sentiment_satisfied' },
  { valor: 'REGULAR', etiqueta: 'Regular', icono: 'sentiment_neutral' },
  { valor: 'MAL', etiqueta: 'Mal', icono: 'sentiment_dissatisfied' },
] as const;
export type EstadoAnimo = (typeof ESTADOS_ANIMO)[number]['valor'];

export interface SesionJuego {
  idSesion?: number;
  idUsuario: number;
  idVideojuego: number;
  idDispositivo?: number | null;
  fechaInicio: string;
  fechaFin?: string | null;
  duracionMinutos?: number | null; // null mientras está EN_CURSO
  pausasRealizadas?: number | null;
  numeroInterrupciones?: number | null;
  estadoSesion?: EstadoSesion;
  anotacionesUsuario?: string | null;
  energiaFisica?: EnergiaFisica | null;
  estadoAnimo?: EstadoAnimo | null;
  efectosPostJuego?: string | null;
  respetoLimites?: boolean | null; // lo calcula el servidor
  nombreJuego?: string; // solo lectura
}
