package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.JugadorJovenDTO;

import java.util.List;

public interface JugadorJovenService {
    List<JugadorJovenDTO> listar();
    JugadorJovenDTO buscarPorId(Long id);
    JugadorJovenDTO insertar(JugadorJovenDTO dto);
    JugadorJovenDTO actualizar(Long id, JugadorJovenDTO dto);
    void eliminar(Long id);
    JugadorJovenDTO buscarPorUsuario(Long idUsuario);
    JugadorJovenDTO buscarPorCodigo(String codigoVinculacion);
    List<JugadorJovenDTO> buscarPorNivelHabilidad(String nivel);

    /** Genera un codigo PC-XXXXXX que aun no existe en jugador_joven. */
    String generarCodigoVinculacion();
}
