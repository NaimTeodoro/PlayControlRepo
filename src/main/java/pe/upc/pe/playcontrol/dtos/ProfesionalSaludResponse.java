package pe.upc.pe.playcontrol.dtos;

import java.time.LocalDateTime;

public record ProfesionalSaludResponse(
        Integer idProfesional,
        Integer idUsuario,
        String especialidad,
        String numeroLicencia,
        String institucion,
        Integer aniosExperiencia,
        String areaTrabajo,
        String certificaciones,
        String telefonoConsulta,
        String horarioConsulta,
        String verificadoPor,
        LocalDateTime fechaVerificacion,
        String estado
) {
}
