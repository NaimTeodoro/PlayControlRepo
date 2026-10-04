package pe.upc.pe.playcontrol.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SessionJuegoDTO {
    private Long idSesion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer duracionMinutos;
    private Integer pausasRealizadas;
    private String estadoSesion;
    private Boolean respetoLimites;
    private Long idVideojuego;
    private Long idUsuario;
}