package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Consumo de un recurso educativo por un usuario. tituloRecurso es de solo lectura.
 * valoracionUsuario: 1 a 5.
 */
@Data
public class UsuarioRecursoEducativoDTO {
    private Long idUsuarioRecurso;
    @NotNull
    private Long idUsuario;
    @NotNull
    private Long idRecurso;
    private LocalDateTime fechaAcceso;
    @Min(0)
    private Integer tiempoVisualizacionMinutos;
    private Boolean completado;
    @Min(1) @Max(5)
    private Integer valoracionUsuario;
    private String comentario;

    // Solo lectura
    private String tituloRecurso;
}
