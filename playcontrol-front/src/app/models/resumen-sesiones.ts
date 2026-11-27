import { SesionJuego } from './sesion-juego';

// ResumenSesionesDTO — /sesiones/usuario/{id}/resumen (semana = lunes a hoy; mes = mes calendario)
export interface ResumenSesiones {
  minutosHoy: number;
  minutosSemana: number;
  minutosMes: number;
  sesionesHoy: number;
  limiteDiarioMinutos: number | null; // null si no hay límite ACTIVO
  porcentajeHoy: number | null;
  sesionEnCurso: SesionJuego | null;
}
