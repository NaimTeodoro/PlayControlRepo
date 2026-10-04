package pe.upc.pe.playcontrol.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecursoEducativoResponse(
        Integer idRecurso,
        String titulo,
        String descripcion,
        String contenido,
        String tipoRecurso,
        String formato,
        Integer duracionMinutos,
        String audienciaObjetivo,
        String categoria,
        String subcategoria,
        String nivelDificultad,
        String urlRecurso,
        String urlImagen,
        String autor,
        LocalDateTime fechaPublicacion,
        LocalDateTime fechaActualizacion,
        Boolean activo,
        Integer visitas,
        BigDecimal valoracion
) {
}
