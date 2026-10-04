package pe.upc.pe.playcontrol.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DispositivoDTO {
    private Long idDispositivo;
    private String tipoDispositivo;
    private String modelo;
    private String sistemaOperativo;
    private String identificadorDisp;
    private LocalDateTime fechaAsociacion;
    private String estado;
    private LocalDateTime ultimaActividad;
    private String versionApp;
    private Long idUsuario; // Relación con el User
}