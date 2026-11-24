package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Perfil de jugador joven. codigoVinculacion se genera al crear el perfil si no se envia (PC-XXXXXX).
 * diasJuegoPreferidos: LUN,MAR,MIE,JUE,VIE,SAB,DOM
 */
@Data
public class JugadorJovenDTO {
    private Long idJugadorJoven;
    @NotNull
    private Long idUsuario;
    @Size(max = 20)
    private String codigoVinculacion;
    @Size(max = 50)
    private String nivelHabilidad;
    @Size(max = 100)
    private String generoPreferido;
    private LocalTime horaPreferidaInicio;
    private LocalTime horaPreferidaFin;
    @Pattern(regexp = DiasSemana.REGEX, message = DiasSemana.MENSAJE)
    private String diasJuegoPreferidos;
    @Size(max = 50)
    private String motivacionPrincipal;
    private String problemasIdentificados;
    private String historialProblemas;
    private LocalDateTime fechaRegistroSegmento;
}
