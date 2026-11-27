package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Recurso educativo. fechaPublicacion, fechaActualizacion, visitas y valoracion los gestiona el servidor.
 * tipoRecurso: ARTICULO / VIDEO / GUIA / FAQ
 * audienciaObjetivo: PADRES / JUGADORES / PROFESIONALES / TODOS
 * nivelDificultad: BASICO / INTERMEDIO / AVANZADO
 */
@Data
public class RecursoEducativoDTO {
    private Long idRecurso;
    @NotBlank @Size(max = 300)
    private String titulo;
    private String descripcion;
    private String contenido;
    @Pattern(regexp = "ARTICULO|VIDEO|GUIA|FAQ")
    private String tipoRecurso;
    @Size(max = 30)
    private String formato;
    @PositiveOrZero
    private Integer duracionMinutos;
    @Pattern(regexp = "PADRES|JUGADORES|PROFESIONALES|TODOS")
    private String audienciaObjetivo;
    @Size(max = 100)
    private String categoria;
    @Size(max = 100)
    private String subcategoria;
    @Pattern(regexp = "BASICO|INTERMEDIO|AVANZADO")
    private String nivelDificultad;
    @Size(max = 500)
    private String urlRecurso;
    @Size(max = 500)
    private String urlImagen;
    @Size(max = 200)
    private String autor;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaActualizacion;
    private Boolean activo;
    private Integer visitas;
    private BigDecimal valoracion;
}
