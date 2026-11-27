package pe.upc.pe.playcontrol.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Usuario para el CRUD de administracion. La contrasena solo se recibe (nunca se devuelve).
 * roles: ROLE_ADMIN / ROLE_PADRE / ROLE_JUGADOR / ROLE_PROFESIONAL
 */
@Data
public class UsuarioDTO {
    private Long idUsuario;
    @NotBlank @Size(max = 100)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 100)
    private String password;
    private Boolean enabled;
    @Size(max = 100)
    private String nombre;
    @Size(max = 100)
    private String apellido;
    @Email @Size(max = 150)
    private String email;
    @Size(max = 20)
    private String telefono;
    private LocalDate fechaNacimiento;
    @Size(max = 20)
    private String genero;
    @Pattern(regexp = "ACTIVO|INACTIVO")
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
    @Size(max = 100)
    private String pais;
    @Size(max = 100)
    private String ciudad;
    @Size(max = 255)
    private String direccion;
    @Pattern(regexp = "BASICO|MEDIO|ALTO")
    private String nivelTecnologia;
    @Size(max = 500)
    private String configuracionPrivacidad;
    private List<@Pattern(regexp = "ROLE_ADMIN|ROLE_PADRE|ROLE_JUGADOR|ROLE_PROFESIONAL") String> roles;
}
