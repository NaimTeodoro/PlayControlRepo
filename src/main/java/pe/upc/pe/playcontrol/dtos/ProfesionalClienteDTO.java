package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Relacion profesional - jugador (paciente). idUsuarioJugador, nombreJugador y apellidoJugador son de solo lectura.
 * estado: ACTIVO / INACTIVO / PENDIENTE (por defecto ACTIVO)
 */
@Data
public class ProfesionalClienteDTO {
    private Long idProfesionalCliente;
    @NotNull
    private Long idProfesional;
    @NotNull
    private Long idJugadorJoven;
    private LocalDateTime fechaAsociacion;
    @Pattern(regexp = "ACTIVO|INACTIVO|PENDIENTE")
    private String estado;
    private String diagnostico;
    private String planTratamiento;
    @Size(max = 100)
    private String frecuenciaConsulta;
    private LocalDateTime proximaConsulta;
    private String notas;

    // Solo lectura: datos del jugador
    private Long idUsuarioJugador;
    private String nombreJugador;
    private String apellidoJugador;
}
