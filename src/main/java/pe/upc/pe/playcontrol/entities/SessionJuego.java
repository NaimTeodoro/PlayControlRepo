package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones_juego")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SessionJuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSesion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "pausas_realizadas")
    private Integer pausasRealizadas;

    @Column(name = "estado_sesion", length = 30)
    private String estadoSesion;

    @Column(name = "respeto_limites")
    private Boolean respetoLimites;

    @ManyToOne
    @JoinColumn(name = "id_videojuego", nullable = false)
    private Juego juego;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;
}