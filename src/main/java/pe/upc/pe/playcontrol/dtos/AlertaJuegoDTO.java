package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Alerta de juego. tiempoTranscurrido y tiempoRespuesta en minutos; limiteAlcanzado en horas.
 * tipoAlerta: LIMITE_DIARIO / AVISO_PREVIO / PAUSA / HORARIO_NOCTURNO / LIMITE_SEMANAL
 * intensidad: BAJA / MEDIA / ALTA. estadoAlerta: ENVIADA / LEIDA / RESPONDIDA
 */
@Data
public class AlertaJuegoDTO {
    private Long idAlerta;
    @NotNull
    private Long idUsuario;
    private Long idSesion;
    @Pattern(regexp = "LIMITE_DIARIO|AVISO_PREVIO|PAUSA|HORARIO_NOCTURNO|LIMITE_SEMANAL")
    private String tipoAlerta;
    @Size(max = 500)
    private String mensaje;
    @Pattern(regexp = "BAJA|MEDIA|ALTA")
    private String intensidad;
    @Min(0)
    private Integer tiempoTranscurrido;
    private BigDecimal limiteAlcanzado;
    @Size(max = 500)
    private String accionSugerida;
    private Boolean respondidoUsuario;
    @Size(max = 500)
    private String respuestaUsuario;
    @Min(0)
    private Integer tiempoRespuesta;
    @Pattern(regexp = "ENVIADA|LEIDA|RESPONDIDA")
    private String estadoAlerta;
    private LocalDateTime fechaEnvio;
}
