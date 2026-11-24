package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Vinculo padre - hijo. Los campos *Hijo son de solo lectura (se rellenan al responder).
 * estado: PENDIENTE / ACTIVO / INACTIVO. tipoRelacion: PADRE / MADRE / TUTOR
 */
@Data
public class PadreHijoDTO {
    private Long idPadreHijo;
    @NotNull
    private Long idPadreTutor;
    @NotNull
    private Long idJugadorJoven;
    @Pattern(regexp = "PADRE|MADRE|TUTOR")
    private String tipoRelacion;
    private LocalDateTime fechaAsociacion;
    @Pattern(regexp = "PENDIENTE|ACTIVO|INACTIVO")
    private String estado;
    @Size(max = 500)
    private String permisosSupervision;
    private Boolean autorizado;

    // Solo lectura: datos del hijo
    private Long idUsuarioHijo;
    private String usernameHijo;
    private String nombreHijo;
    private String apellidoHijo;
    private String codigoVinculacionHijo;
}
