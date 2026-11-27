// RecompensaDTO — /recompensas: progreso de un usuario en un logro

export const ESTADOS_RECOMPENSA = [
  { valor: 'EN_PROGRESO', etiqueta: 'En progreso' },
  { valor: 'OBTENIDA', etiqueta: 'Obtenida' },
  { valor: 'RECLAMADA', etiqueta: 'Reclamada' },
] as const;
export type EstadoRecompensa = (typeof ESTADOS_RECOMPENSA)[number]['valor'];

export interface Recompensa {
  idRecompensa?: number;
  idUsuario: number;
  idLogro: number;
  estado?: EstadoRecompensa;
  porcentajeProgreso?: number | null;
  fechaObtenida?: string | null;
  fechaReclamada?: string | null;
  notificacionEnviada?: boolean;
  // Solo lectura: datos del logro
  nombreLogro?: string;
  descripcionLogro?: string | null;
  puntosAsignados?: number;
  imagenUrl?: string | null;
}
