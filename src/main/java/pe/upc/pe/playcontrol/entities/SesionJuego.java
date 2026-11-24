package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Sesion de juego de un usuario (antes SessionJuego).
 * estadoSesion: EN_CURSO / FINALIZADA / INTERRUMPIDA (EN_CURSO tiene fechaFin y duracionMinutos null)
 * energiaFisica: BAJA / NORMAL / ALTA
 * estadoAnimo: BIEN / REGULAR / MAL
 */
@Entity
@Table(name = "sesion_juego")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SesionJuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Long idSesion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_videojuego", nullable = false)
    private Videojuego videojuego;

    @ManyToOne
    @JoinColumn(name = "id_dispositivo")
    private Dispositivo dispositivo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "pausas_realizadas")
    private Integer pausasRealizadas;

    @Column(name = "numero_interrupciones")
    private Integer numeroInterrupciones;

    @Column(name = "estado_sesion", length = 30)
    private String estadoSesion;

    @Column(name = "anotaciones_usuario", columnDefinition = "TEXT")
    private String anotacionesUsuario;

    @Column(name = "energia_fisica", length = 20)
    private String energiaFisica;

    @Column(name = "estado_animo", length = 20)
    private String estadoAnimo;

    @Column(name = "efectos_post_juego", columnDefinition = "TEXT")
    private String efectosPostJuego;

    @Column(name = "respeto_limites")
    private Boolean respetoLimites;
}
