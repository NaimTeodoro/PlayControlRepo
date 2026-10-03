package pe.upc.pe.playcontrol.dtos;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String email;
    private String rol; // Ejemplo: "ROLE_USER" o "ROLE_ADMIN"
}
