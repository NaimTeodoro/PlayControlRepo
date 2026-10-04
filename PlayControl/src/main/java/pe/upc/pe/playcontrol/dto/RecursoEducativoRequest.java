package pe.upc.pe.playcontrol.dto;

import jakarta.validation.constraints.*;

public record RecursoEducativoRequest(
        @NotBlank @Size(max = 300) String titulo,
        String descripcion,
        @Size(max = 300) String contenido,
        @Size(max = 50) String tipoRecurso,
        @Size(max = 30) String formato,
        @PositiveOrZero Integer duracionMinutos,
        @Size(max = 50) String audienciaObjetivo,
        @Size(max = 100) String categoria,
        @Size(max = 100) String subcategoria,
        @Size(max = 20) String nivelDificultad,
        @Size(max = 500) String urlRecurso,
        @Size(max = 500) String urlImagen,
        @Size(max = 200) String autor,
        Boolean activo
) {
}
