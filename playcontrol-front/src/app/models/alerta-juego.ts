// AlertaJuegoDTO — /alertas-juego (valores permitidos: sección 5 del contrato)

export const TIPOS_ALERTA = [
  { valor: 'LIMITE_DIARIO', etiqueta: 'Límite diario', icono: 'hourglass_bottom' },
  { valor: 'AVISO_PREVIO', etiqueta: 'Aviso previo', icono: 'alarm' },
  { valor: 'PAUSA', etiqueta: 'Pausa', icono: 'pause_circle' },
  { valor: 'HORARIO_NOCTURNO', etiqueta: 'Horario nocturno', icono: 'bedtime' },
  { valor: 'LIMITE_SEMANAL', etiqueta: 'Límite semanal', icono: 'date_range' },
] as const;
export type TipoAlerta = (typeof TIPOS_ALERTA)[number]['valor'];

export const INTENSIDADES = [
  { valor: 'BAJA', etiqueta: 'Baja' },
  { valor: 'MEDIA', etiqueta: 'Media' },
  { valor: 'ALTA', etiqueta: 'Alta' },
] as const;
export type Intensidad = (typeof INTENSIDADES)[number]['valor'];

export const ESTADOS_ALERTA = [
  { valor: 'ENVIADA', etiqueta: 'Enviada' },
  { valor: 'LEIDA', etiqueta: 'Leída' },
  { valor: 'RESPONDIDA', etiqueta: 'Respondida' },
] as const;
export type EstadoAlerta = (typeof ESTADOS_ALERTA)[number]['valor'];

export interface AlertaJuego {
  idAlerta?: number;
  idUsuario: number;
  idSesion?: number | null;
  tipoAlerta: TipoAlerta;
  mensaje: string;
  intensidad?: Intensidad | null;
  tiempoTranscurrido?: number | null; // minutos
  limiteAlcanzado?: number | null; // horas (BigDecimal)
  accionSugerida?: string | null;
  respondidoUsuario?: boolean;
  respuestaUsuario?: string | null;
  tiempoRespuesta?: number | null; // minutos desde fechaEnvio
  estadoAlerta?: EstadoAlerta;
  fechaEnvio?: string;
}

/** Body de PUT /alertas-juego/{id}/responder */
export interface ResponderAlerta {
  respuestaUsuario: string; // máx. 500
}
