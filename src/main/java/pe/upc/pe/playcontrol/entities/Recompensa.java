package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Logro de cada usuario (progreso del usuario sobre un logro del catalogo).
 * estado: EN_PROGRESO / OBTENIDA / RECLAMADA
 * porcentajeProgreso: 0 a 100. OBTENIDA implica 100 y fechaObtenida; RECLAMADA implica ademas fechaReclamada.
 */
@Entity
@Table(name = "recompensa", uniqueConstraints = {
        @UniqueConstraint(name = "uk_recompensa_usuario_logro", columnNames = {"id_usuario", "id_logro"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Recompensa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recompensa")
    private Long idRecompensa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_logro", nullable = false)
    private Logro logro;

    @Column(name = "estado", length = 30)
    private String estado;

    @Column(name = "porcentaje_progreso")
    private Integer porcentajeProgreso;

    @Column(name = "fecha_obtenida")
    private LocalDateTime fechaObtenida;

    @Column(name = "fecha_reclamada")
    private LocalDateTime fechaReclamada;

    @Column(name = "notificacion_enviada")
    private Boolean notificacionEnviada;
}
