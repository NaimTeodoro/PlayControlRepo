import { EstadoActivo } from './dispositivo';

// LimiteJuegoDTO — /limites-juego (solo un ACTIVO por usuario: al guardar uno ACTIVO, los demás pasan a INACTIVO)

export const METODOS_BLOQUEO = [
  { valor: 'NOTIFICACION', etiqueta: 'Solo notificación' },
  { valor: 'BLOQUEO_SIMPLE', etiqueta: 'Bloqueo simple' },
  { valor: 'BLOQUEO_TOTAL', etiqueta: 'Bloqueo total' },
] as const;
export type MetodoBloqueo = (typeof METODOS_BLOQUEO)[number]['valor'];

export const ACCIONES_AL_EXCEDER = [
  { valor: 'ALERTAR', etiqueta: 'Enviar una alerta' },
  { valor: 'BLOQUEAR', etiqueta: 'Bloquear el juego' },
] as const;
export type AccionAlExceder = (typeof ACCIONES_AL_EXCEDER)[number]['valor'];

export interface LimiteJuego {
  idLimite?: number;
  idUsuario: number;
  idCreadoPor?: number | null; // no se envía en POST (lo asigna el servidor)
  limiteHorasDiarias?: number | null;
  limiteHorasSemanal?: number | null;
  limiteHorasMensual?: number | null;
  horaInicioDia?: string | null; // HH:mm:ss
  horaFinDia?: string | null;
  diasPermitidos?: string | null; // "LUN,MAR,MIE"
  minutosAvisoPrevio?: number | null;
  metodoBloqueo?: MetodoBloqueo | null;
  accionAlExceder?: AccionAlExceder | null;
  estado?: EstadoActivo;
  fechaCreacion?: string | null;
  ultimaModificacion?: string | null;
  observaciones?: string | null;
}
