// ReportePadreDTO — /reportes-padre (PADRE y ADMIN)

export const PERIODOS_REPORTE = [
  { valor: 'SEMANAL', etiqueta: 'Semanal' },
  { valor: 'MENSUAL', etiqueta: 'Mensual' },
] as const;
export type PeriodoReporte = (typeof PERIODOS_REPORTE)[number]['valor'];

export const TENDENCIAS = [
  { valor: 'MEJORA', etiqueta: 'Mejora', icono: 'trending_down' },
  { valor: 'ESTABLE', etiqueta: 'Estable', icono: 'trending_flat' },
  { valor: 'EMPEORA', etiqueta: 'Empeora', icono: 'trending_up' },
] as const;
export type Tendencia = (typeof TENDENCIAS)[number]['valor'];

export interface ReportePadre {
  idReporte?: number;
  idPadreHijo: number;
  fechaGeneracion?: string | null;
  periodoReporte?: PeriodoReporte;
  fechaInicioPeriodo?: string | null; // yyyy-MM-dd
  fechaFinPeriodo?: string | null;
  tiempoTotalJuegoMinutos?: number | null;
  tiempoPromedioSesionMinutos?: number | null;
  sesionesJuegoTotal?: number | null;
  videojuegosJugados?: number | null;
  diasActivos?: number | null;
  diasSinJuego?: number | null;
  respetoLimitesPorc?: number | null;
  sesionesRespetuosas?: number | null;
  alertasRecibidas?: number | null;
  alertasRespondidas?: number | null;
  logrosAlcanzados?: number | null;
  puntosObtenidos?: number | null;
  rachaDias?: number | null;
  recomendacionesSistema?: string | null;
  alertasImportantes?: string | null;
  tendencia?: Tendencia | null;
  enviado?: boolean;
  fechaEnvio?: string | null;
  visto?: boolean;
  fechaVisto?: string | null;
  // Solo lectura
  idPadreTutor?: number;
  idJugadorJoven?: number;
  nombreHijo?: string;
}
