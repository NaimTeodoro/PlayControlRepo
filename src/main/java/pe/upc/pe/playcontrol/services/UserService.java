package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.UserRegisterDTO;
import pe.upc.pe.playcontrol.dtos.UsuarioActualDTO;
import pe.upc.pe.playcontrol.dtos.UsuarioDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UsuarioDTO> listar();
    UsuarioDTO buscarPorId(Long id);
    UsuarioDTO insertar(UsuarioDTO dto);
    UsuarioDTO actualizar(Long id, UsuarioDTO dto);
    void eliminar(Long id);

    /** Registro publico: crea el usuario y su perfil (jugador, padre o profesional) en la misma transaccion. */
    Map<String, Object> registrar(UserRegisterDTO dto);

    /** Datos del usuario autenticado para GET /api/auth/me. */
    UsuarioActualDTO obtenerActual(String username);
}
