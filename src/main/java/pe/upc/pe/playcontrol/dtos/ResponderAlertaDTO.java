package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Body de PUT /api/alertas-juego/{id}/responder.
 */
@Data
public class ResponderAlertaDTO {
    @NotBlank @Size(max = 500)
    private String respuestaUsuario;
}
