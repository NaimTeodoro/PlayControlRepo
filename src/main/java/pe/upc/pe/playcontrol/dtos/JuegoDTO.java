package pe.upc.pe.playcontrol.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class JuegoDTO {
    private Long idVideojuego;
    private String nombreJuego;
    private String genero;
    private String desarrollador;
    private LocalDate fechaLanzamiento;
    private String clasificacionPEGI;
    private String descripcion;
    private Integer tiempoPromedioSesion;
    private Boolean activo;
    private Long idPlataforma;
}
