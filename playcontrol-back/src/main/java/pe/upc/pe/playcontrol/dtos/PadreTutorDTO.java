package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Perfil de padre o tutor. Los hijos se consultan en /api/padre-hijo/padre/{idPadreTutor}.
 */
@Data
public class PadreTutorDTO {
    private Long idPadreTutor;
    @NotNull
    private Long idUsuario;
    private String metodosControlActual;
    private String preocupacionesPrincipales;
    private String objetivosSupervision;
    @Pattern(regexp = "DIARIA|SEMANAL|MENSUAL")
    private String frecuenciaRevisionReportes;
    private String configuracionAlertas;
    private LocalDateTime fechaRegistro;
}
