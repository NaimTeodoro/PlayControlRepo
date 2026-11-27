package pe.upc.pe.playcontrol.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String jwtToken;
    private Long idUsuario;
    private String username;
    // Ej: ["ROLE_JUGADOR"]. El front lo usa para decidir qué vista mostrar.
    private List<String> roles;
}
