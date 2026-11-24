package pe.upc.pe.playcontrol.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Respuesta de GET /api/auth/me. Los id de perfil son null si no aplican al usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioActualDTO {
    private Long idUsuario;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private List<String> roles;
    private Long idJugadorJoven;
    private Long idPadreTutor;
    private Long idProfesional;
}
