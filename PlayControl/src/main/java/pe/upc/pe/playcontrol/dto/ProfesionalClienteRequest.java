package pe.upc.pe.playcontrol.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record ProfesionalClienteRequest(
        @NotNull @Positive Integer idProfesional,
        @NotNull @Positive Integer idJugadorJoven,
        @Size(max = 20) String estado,
        String diagnostico,
        String planTratamiento,
        @Size(max = 100) String frecuenciaConsulta,
        LocalDateTime proximaConsulta,
        String notas
) {
}
