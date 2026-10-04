package pe.upc.pe.playcontrol.dtos;
import lombok.Data;
import java.time.LocalDate;
@Data
public class RetoUsuarioDTO {
    private Long idRetoUsuario;
    private String estado;
    private LocalDate fechaCompletado;


    private Long idUsuario;
    private Long idReto;
}