package pe.upc.pe.playcontrol.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record ProfesionalSaludRequest(
        @NotNull @Positive Integer idUsuario,
        @NotBlank @Size(max = 150) String especialidad,
        @NotBlank @Size(max = 100) String numeroLicencia,
        @Size(max = 200) String institucion,
        @PositiveOrZero Integer aniosExperiencia,
        @Size(max = 50) String areaTrabajo,
        String certificaciones,
        @Size(max = 20) String telefonoConsulta,
        @Size(max = 200) String horarioConsulta,
        @Size(max = 100) String verificadoPor,
        LocalDateTime fechaVerificacion,
        @Size(max = 20) String estado
) {
}
