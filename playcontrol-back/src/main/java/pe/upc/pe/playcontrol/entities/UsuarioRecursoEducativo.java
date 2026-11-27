package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Consumo de un recurso educativo por parte de un usuario.
 * valoracionUsuario: 1 a 5.
 */
@Entity
@Table(name = "usuario_recurso_educativo", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_recurso", columnNames = {"id_usuario", "id_recurso"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioRecursoEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_recurso")
    private Long idUsuarioRecurso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_recurso", nullable = false)
    private RecursoEducativo recurso;

    @Column(name = "fecha_acceso")
    private LocalDateTime fechaAcceso;

    @Column(name = "tiempo_visualizacion_minutos")
    private Integer tiempoVisualizacionMinutos;

    @Column(name = "completado")
    private Boolean completado;

    @Column(name = "valoracion_usuario")
    private Integer valoracionUsuario;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;
}
