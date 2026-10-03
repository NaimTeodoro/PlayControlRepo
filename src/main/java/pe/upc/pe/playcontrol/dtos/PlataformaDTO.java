package pe.upc.pe.playcontrol.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlataformaDTO {
    private Long idPlataforma;
    private String nombrePlataforma;
    private String tipo;
    private String descripcion;
    private String desarrollador;
    private String sistemaOperativo;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
}