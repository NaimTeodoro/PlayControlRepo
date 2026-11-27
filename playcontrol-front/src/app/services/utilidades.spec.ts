import { AlertaJuego, EstadoAlerta } from '../models/alerta-juego';
import { EstadoSesion, SesionJuego } from '../models/sesion-juego';
import { claseIntensidad, esPendiente, ordenarAlertas } from './alerta-service';
import { claseProgreso, porcentajeLimite } from './limite-service';
import {
  fechaLocal,
  formatoDuracion,
  isoLocal,
  minutosPorDia,
  minutosSesion,
} from './sesion-service';

// Sesión de ejemplo (SesionJuegoDTO plano de V2)
const sesion = (
  inicio: string,
  minutos: number | null,
  estado: EstadoSesion = 'FINALIZADA',
): SesionJuego => ({
  idUsuario: 4,
  idVideojuego: 1,
  fechaInicio: inicio,
  duracionMinutos: minutos,
  estadoSesion: estado,
});

describe('Fechas y duración', () => {
  it('formatea minutos como horas y minutos', () => {
    expect(formatoDuracion(0)).toBe('0 min');
    expect(formatoDuracion(45)).toBe('45 min');
    expect(formatoDuracion(60)).toBe('1 h');
    expect(formatoDuracion(85)).toBe('1 h 25 min');
  });

  it('genera fechas locales sin zona horaria', () => {
    const d = new Date(2026, 10, 7, 9, 5, 3); // 7 nov 2026 09:05:03
    expect(fechaLocal(d)).toBe('2026-11-07');
    expect(isoLocal(d)).toBe('2026-11-07T09:05:03');
  });

  it('una sesión EN_CURSO cuenta el tiempo transcurrido', () => {
    const hace30 = isoLocal(new Date(Date.now() - 30 * 60000));
    expect(minutosSesion(sesion(hace30, null, 'EN_CURSO'))).toBeGreaterThanOrEqual(29);
    expect(minutosSesion(sesion(hace30, null, 'EN_CURSO'))).toBeLessThanOrEqual(31);
    expect(minutosSesion(sesion('2026-11-01T10:00:00', 45))).toBe(45);
  });
});

describe('Minutos por día (gráfico de monitoreo)', () => {
  // Martes 17 de noviembre de 2026
  const ahora = new Date(2026, 10, 17, 12, 0, 0);

  it('devuelve los últimos 14 días terminando en hoy', () => {
    const dias = minutosPorDia([sesion('2026-11-17T03:00:00', 45)], 14, ahora);
    expect(dias.length).toBe(14);
    expect(dias[13]).toEqual({ fecha: '2026-11-17', etiqueta: 'M', minutos: 45 });
    expect(dias[0].fecha).toBe('2026-11-04');
  });

  it('suma varias sesiones del mismo día e ignora las de fuera del rango', () => {
    const dias = minutosPorDia(
      [
        sesion('2026-11-16T19:00:00', 37),
        sesion('2026-11-16T21:00:00', 20),
        sesion('2026-10-01T18:00:00', 60), // fuera de los 7 días
      ],
      7,
      ahora,
    );
    expect(dias.find((d) => d.fecha === '2026-11-16')?.minutos).toBe(57);
    expect(dias.reduce((t, d) => t + d.minutos, 0)).toBe(57);
  });
});

describe('Progreso de límites', () => {
  it('elige el color según el porcentaje', () => {
    expect(claseProgreso(69)).toBe('progreso-ok');
    expect(claseProgreso(70)).toBe('progreso-aviso');
    expect(claseProgreso(99)).toBe('progreso-aviso');
    expect(claseProgreso(100)).toBe('progreso-pasado');
  });

  it('calcula el porcentaje frente a un límite en horas', () => {
    expect(porcentajeLimite(60, 2)).toBe(50);
    expect(porcentajeLimite(100, 1.5)).toBeCloseTo(111.1, 1);
    expect(porcentajeLimite(30, null)).toBeNull();
    expect(porcentajeLimite(30, 0)).toBeNull();
  });
});

describe('Alertas', () => {
  const alerta = (
    id: number,
    estado: EstadoAlerta,
    fecha: string,
    respondida = false,
  ): AlertaJuego => ({
    idAlerta: id,
    idUsuario: 4,
    tipoAlerta: 'LIMITE_DIARIO',
    mensaje: 'x',
    estadoAlerta: estado,
    fechaEnvio: fecha,
    respondidoUsuario: respondida,
  });

  it('sin responder es pendiente (aunque ya esté LEIDA)', () => {
    expect(esPendiente(alerta(1, 'ENVIADA', '2026-11-01T10:00:00'))).toBe(true);
    expect(esPendiente(alerta(2, 'LEIDA', '2026-11-01T10:00:00'))).toBe(true);
    expect(esPendiente(alerta(3, 'RESPONDIDA', '2026-11-01T10:00:00', true))).toBe(false);
  });

  it('ordena pendientes primero y luego por fecha descendente', () => {
    const orden = ordenarAlertas([
      alerta(1, 'RESPONDIDA', '2026-11-16T10:00:00', true),
      alerta(2, 'ENVIADA', '2026-11-10T10:00:00'),
      alerta(3, 'ENVIADA', '2026-11-15T10:00:00'),
    ]).map((a) => a.idAlerta);
    expect(orden).toEqual([3, 2, 1]);
  });

  it('colorea la intensidad', () => {
    expect(claseIntensidad('ALTA')).toBe('peligro');
    expect(claseIntensidad('MEDIA')).toBe('alerta');
    expect(claseIntensidad('BAJA')).toBe('exito');
  });
});
