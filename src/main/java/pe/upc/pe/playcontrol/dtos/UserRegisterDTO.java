package pe.upc.pe.playcontrol.dtos;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String email;
    private String nombre;
    private String apellido;
    // Valores permitidos: ROLE_JUGADOR, ROLE_PADRE, ROLE_PROFESIONAL (por defecto ROLE_JUGADOR)
    private String rol;
}
