// JugadorJovenDTO — /jugadores-jovenes (idJugadorJoven es distinto de idUsuario)

// Días de la semana: diasJuegoPreferidos y diasPermitidos van como "LUN,MAR,..." sin espacios
export const DIAS_SEMANA = [
  { valor: 'LUN', etiqueta: 'Lun' },
  { valor: 'MAR', etiqueta: 'Mar' },
  { valor: 'MIE', etiqueta: 'Mié' },
  { valor: 'JUE', etiqueta: 'Jue' },
  { valor: 'VIE', etiqueta: 'Vie' },
  { valor: 'SAB', etiqueta: 'Sáb' },
  { valor: 'DOM', etiqueta: 'Dom' },
] as const;
export type DiaSemana = (typeof DIAS_SEMANA)[number]['valor'];

export interface JugadorJoven {
  idJugadorJoven?: number;
  idUsuario: number;
  codigoVinculacion?: string; // PC-XXXXXX (lo genera el servidor y no cambia en PUT)
  nivelHabilidad?: string | null;
  generoPreferido?: string | null;
  horaPreferidaInicio?: string | null; // HH:mm:ss
  horaPreferidaFin?: string | null;
  diasJuegoPreferidos?: string | null;
  motivacionPrincipal?: string | null;
  problemasIdentificados?: string | null;
  historialProblemas?: string | null;
  fechaRegistroSegmento?: string | null;
}
