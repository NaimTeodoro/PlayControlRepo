package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Sesion de juego. nombreJuego es de solo lectura.
 * estadoSesion: EN_CURSO / FINALIZADA / INTERRUMPIDA (EN_CURSO: fechaFin y duracionMinutos null)
 * energiaFisica: BAJA / NORMAL / ALTA. estadoAnimo: BIEN / REGULAR / MAL
 */
@Data
public class SesionJuegoDTO {
    private Long idSesion;
    @NotNull
    private Long idUsuario;
    @NotNull
    private Long idVideojuego;
    private Long idDispositivo;
    @NotNull
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    @PositiveOrZero
    private Integer duracionMinutos;
    @Min(0)
    private Integer pausasRealizadas;
    @Min(0)
    private Integer numeroInterrupciones;
    @Pattern(regexp = "EN_CURSO|FINALIZADA|INTERRUMPIDA")
    private String estadoSesion;
    private String anotacionesUsuario;
    @Pattern(regexp = "BAJA|NORMAL|ALTA")
    private String energiaFisica;
    @Pattern(regexp = "BIEN|REGULAR|MAL")
    private String estadoAnimo;
    private String efectosPostJuego;
    private Boolean respetoLimites;

    // Solo lectura
    private String nombreJuego;
}
