package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Logro de un usuario. nombreLogro, descripcionLogro, puntosAsignados e imagenUrl son de solo lectura (del logro).
 * estado: EN_PROGRESO / OBTENIDA / RECLAMADA. porcentajeProgreso: 0 a 100.
 */
@Data
public class RecompensaDTO {
    private Long idRecompensa;
    @NotNull
    private Long idUsuario;
    @NotNull
    private Long idLogro;
    @Pattern(regexp = "EN_PROGRESO|OBTENIDA|RECLAMADA")
    private String estado;
    @Min(0) @Max(100)
    private Integer porcentajeProgreso;
    private LocalDateTime fechaObtenida;
    private LocalDateTime fechaReclamada;
    private Boolean notificacionEnviada;

    // Solo lectura: datos del logro
    private String nombreLogro;
    private String descripcionLogro;
    private Integer puntosAsignados;
    private String imagenUrl;
}
