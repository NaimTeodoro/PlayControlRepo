package pe.upc.pe.playcontrol.dtos;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RetoDTO {
    private Long idReto;
    private String nombre;
    private String descripcion;
    private Integer puntosOtorgados;
}
