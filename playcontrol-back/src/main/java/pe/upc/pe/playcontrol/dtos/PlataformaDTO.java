package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Plataforma de juego. tipo: PC / CONSOLA / MOVIL
 */
@Data
public class PlataformaDTO {
    private Long idPlataforma;
    @NotBlank @Size(max = 100)
    private String nombrePlataforma;
    @Pattern(regexp = "PC|CONSOLA|MOVIL")
    private String tipo;
    private String descripcion;
    @Size(max = 150)
    private String desarrollador;
    @Size(max = 100)
    private String sistemaOperativo;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
}
