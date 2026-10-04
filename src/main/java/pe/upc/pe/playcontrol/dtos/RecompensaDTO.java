package pe.upc.pe.playcontrol.dtos;
import lombok.Data;
import java.time.LocalDate;
@Data
public class RecompensaDTO {
    private Long idRecompensa;
    private String nombre;
    private String descripcion;
    private Integer costoPuntos;
}
