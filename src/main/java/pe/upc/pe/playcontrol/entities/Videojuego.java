package pe.upc.pe.playcontrol.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Videojuego del catalogo (antes Juego).
 * clasificacionPEGI: 3 / 7 / 12 / 16 / 18
 * tiempoPromedioSesion en minutos.
 */
@Entity
@Table(name = "videojuego")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Videojuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_videojuego")
    private Long idVideojuego;

    @ManyToOne
    @JoinColumn(name = "id_plataforma", nullable = false)
    private Plataforma plataforma;

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

    @Column(name = "url_imagen", length = 500)
    private String urlImagen;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
