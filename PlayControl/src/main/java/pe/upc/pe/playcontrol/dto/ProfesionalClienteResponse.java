package pe.upc.pe.playcontrol.dto;

import java.time.LocalDateTime;

public record ProfesionalClienteResponse(
        Integer idProfesionalCliente,
        Integer idProfesional,
        Integer idJugadorJoven,
        LocalDateTime fechaAsociacion,
        String estado,
        String diagnostico,
        String planTratamiento,
        String frecuenciaConsulta,
        LocalDateTime proximaConsulta,
        String notas
) {
}
