package pe.upc.pe.playcontrol.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta de GET /api/sesiones/usuario/{idUsuario}/resumen.
 * semana = lunes a hoy; una sesion EN_CURSO suma los minutos transcurridos hasta ahora.
 * limiteDiarioMinutos = limiteHorasDiarias * 60 del limite ACTIVO (null si no hay).
 * porcentajeHoy = minutosHoy * 100 / limiteDiarioMinutos (null si no hay limite).
 * sesionEnCurso = la sesion EN_CURSO del usuario o null.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenSesionesDTO {
    private Integer minutosHoy;
    private Integer minutosSemana;
    private Integer minutosMes;
    private Integer sesionesHoy;
    private Integer limiteDiarioMinutos;
    private Integer porcentajeHoy;
    private SesionJuegoDTO sesionEnCurso;
}
