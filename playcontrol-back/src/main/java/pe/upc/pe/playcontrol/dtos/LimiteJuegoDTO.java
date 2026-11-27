package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Limite de juego. idUsuario = jugador al que aplica; idCreadoPor = jugador o padre que lo configuro.
 * diasPermitidos: LUN,MAR,MIE,JUE,VIE,SAB,DOM
 * metodoBloqueo: NOTIFICACION / BLOQUEO_SIMPLE / BLOQUEO_TOTAL. accionAlExceder: ALERTAR / BLOQUEAR
 * estado: ACTIVO / INACTIVO. Al guardar uno ACTIVO, los demas activos del usuario pasan a INACTIVO.
 */
@Data
public class LimiteJuegoDTO {
    private Long idLimite;
    @NotNull
    private Long idUsuario;
    private Long idCreadoPor;
    @DecimalMin("0.00") @DecimalMax("24.00")
    private BigDecimal limiteHorasDiarias;
    @DecimalMin("0.00") @DecimalMax("168.00")
    private BigDecimal limiteHorasSemanal;
    @DecimalMin("0.00") @DecimalMax("744.00")
    private BigDecimal limiteHorasMensual;
    private LocalTime horaInicioDia;
    private LocalTime horaFinDia;
    @Pattern(regexp = DiasSemana.REGEX, message = DiasSemana.MENSAJE)
    private String diasPermitidos;
    @Min(0) @Max(120)
    private Integer minutosAvisoPrevio;
    @Pattern(regexp = "NOTIFICACION|BLOQUEO_SIMPLE|BLOQUEO_TOTAL")
    private String metodoBloqueo;
    @Pattern(regexp = "ALERTAR|BLOQUEAR")
    private String accionAlExceder;
    @Pattern(regexp = "ACTIVO|INACTIVO")
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimaModificacion;
    private String observaciones;
}
