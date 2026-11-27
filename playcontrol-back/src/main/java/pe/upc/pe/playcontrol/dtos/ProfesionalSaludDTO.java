package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Perfil de profesional de salud. estado: ACTIVO / INACTIVO / PENDIENTE (por defecto ACTIVO).
 */
@Data
public class ProfesionalSaludDTO {
    private Long idProfesional;
    @NotNull
    private Long idUsuario;
    @NotBlank @Size(max = 150)
    private String especialidad;
    @NotBlank @Size(max = 100)
    private String numeroLicencia;
    @Size(max = 200)
    private String institucion;
    @PositiveOrZero
    private Integer aniosExperiencia;
    @Size(max = 50)
    private String areaTrabajo;
    private String certificaciones;
    @Size(max = 20)
    private String telefonoConsulta;
    @Size(max = 200)
    private String horarioConsulta;
    @Size(max = 100)
    private String verificadoPor;
    private LocalDateTime fechaVerificacion;
    @Pattern(regexp = "ACTIVO|INACTIVO|PENDIENTE")
    private String estado;
}
