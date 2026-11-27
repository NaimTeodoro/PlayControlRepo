package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Historial de una alerta. idUsuario, tipoAlerta y respuestaUsuario son de solo lectura (se leen de la alerta).
 * accionRealizada: TERMINO_SESION / PAUSA / IGNORADA. efectividadAlerta: ALTA / MEDIA / BAJA. estado: ABIERTO / CERRADO
 */
@Data
public class HistorialAlertaDTO {
    private Long idHistorial;
    @NotNull
    private Long idAlerta;
    private String descripcion;
    @Size(max = 500)
    private String accionRealizada;
    @Size(max = 200)
    private String impactoEnSesion;
    @Pattern(regexp = "ALTA|MEDIA|BAJA")
    private String efectividadAlerta;
    private LocalDateTime fecha;
    @Pattern(regexp = "ABIERTO|CERRADO")
    private String estado;
    private String notasAdicionales;

    // Solo lectura: datos de la alerta
    private Long idUsuario;
    private String tipoAlerta;
    private String respuestaUsuario;
}
