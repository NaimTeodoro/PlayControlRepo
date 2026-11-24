package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Videojuego del catalogo. tiempoPromedioSesion en minutos.
 */
@Data
public class VideojuegoDTO {
    private Long idVideojuego;
    @NotNull
    private Long idPlataforma;
    @NotBlank @Size(max = 200)
    private String nombreJuego;
    @Size(max = 100)
    private String genero;
    @Size(max = 150)
    private String desarrollador;
    private LocalDate fechaLanzamiento;
    @Size(max = 5)
    private String clasificacionPEGI;
    private String descripcion;
    @PositiveOrZero
    private Integer tiempoPromedioSesion;
    @Size(max = 500)
    private String urlImagen;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
}
