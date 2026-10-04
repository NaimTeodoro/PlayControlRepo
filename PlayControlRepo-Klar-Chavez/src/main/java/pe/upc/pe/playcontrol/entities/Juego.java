package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "videojuegos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVideojuego;

    @Column(name = "nombre_juego", nullable = false, length = 200)
    private String nombreJuego;

    @Column(name = "genero", length = 100)
    private String genero;

    @Column(name = "desarrollador", length = 150)
    private String desarrollador;

    @Column(name = "fecha_lanzamiento")
    private LocalDate fechaLanzamiento;

    @Column(name = "clasificacion_pegi", length = 5)
    private String clasificacionPEGI;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "tiempo_promedio_sesion")
    private Integer tiempoPromedioSesion;

    @Column(name = "activo")
    private Boolean activo;

    @ManyToOne
    @JoinColumn(name = "id_plataforma", nullable = false)
    private Plataforma plataforma;
}
