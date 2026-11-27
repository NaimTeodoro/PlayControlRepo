package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Logro del catalogo.
 * tipo: DIARIO / SEMANAL / HITO
 * condicionTipo: DIAS_RESPETANDO_LIMITE / PAUSAS_REALIZADAS / DIAS_SIN_JUEGO / SESIONES_CORTAS / RECURSOS_COMPLETADOS
 */
@Data
public class LogroDTO {
    private Long idLogro;
    @NotBlank @Size(max = 200)
    private String nombreLogro;
    private String descripcion;
    @NotNull @Min(0)
    private Integer puntosAsignados;
    @Pattern(regexp = "DIARIO|SEMANAL|HITO")
    private String tipo;
    private String condicion;
    @Pattern(regexp = "DIAS_RESPETANDO_LIMITE|PAUSAS_REALIZADAS|DIAS_SIN_JUEGO|SESIONES_CORTAS|RECURSOS_COMPLETADOS")
    private String condicionTipo;
    @Min(0)
    private Integer valorCondicion;
    @Size(max = 500)
    private String imagenUrl;
    private Boolean activo;
}
