package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Dispositivo de un usuario. tipoDispositivo: PC / MOVIL / CONSOLA. estado: ACTIVO / INACTIVO
 */
@Data
public class DispositivoDTO {
    private Long idDispositivo;
    @NotNull
    private Long idUsuario;
    @Pattern(regexp = "PC|MOVIL|CONSOLA")
    private String tipoDispositivo;
    @Size(max = 150)
    private String modelo;
    @Size(max = 100)
    private String sistemaOperativo;
    @Size(max = 100)
    private String identificadorDisp;
    private LocalDateTime fechaAsociacion;
    @Pattern(regexp = "ACTIVO|INACTIVO")
    private String estado;
    private LocalDateTime ultimaActividad;
    @Size(max = 20)
    private String versionApp;
}
