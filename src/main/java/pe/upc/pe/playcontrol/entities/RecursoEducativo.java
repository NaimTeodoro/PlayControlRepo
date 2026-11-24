package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Recurso educativo (articulos, videos, guias y FAQ).
 * tipoRecurso: ARTICULO / VIDEO / GUIA / FAQ
 * audienciaObjetivo: PADRES / JUGADORES / PROFESIONALES / TODOS
 * nivelDificultad: BASICO / INTERMEDIO / AVANZADO
 */
@Entity
@Table(name = "recurso_educativo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RecursoEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Long idRecurso;

    @Column(name = "titulo", length = 300, nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "tipo_recurso", length = 50)
    private String tipoRecurso;

    @Column(name = "formato", length = 30)
    private String formato;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "audiencia_objetivo", length = 50)
    private String audienciaObjetivo;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "subcategoria", length = 100)
    private String subcategoria;

    @Column(name = "nivel_dificultad", length = 20)
    private String nivelDificultad;

    @Column(name = "url_recurso", length = 500)
    private String urlRecurso;

    @Column(name = "url_imagen", length = 500)
    private String urlImagen;

    @Column(name = "autor", length = 200)
    private String autor;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "visitas", nullable = false)
    private Integer visitas;

    @Column(name = "valoracion", precision = 3, scale = 2)
    private BigDecimal valoracion;
}
