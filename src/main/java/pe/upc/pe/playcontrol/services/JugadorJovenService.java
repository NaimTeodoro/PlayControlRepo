package pe.upc.pe.playcontrol.services;

import java.util.List;
import pe.upc.pe.playcontrol.dtos.JugadorJovenDTO;

public interface JugadorJovenService {
    List<JugadorJovenDTO> listar();

    JugadorJovenDTO buscarPorId(Long id);

    JugadorJovenDTO insertar(JugadorJovenDTO dto);

    JugadorJovenDTO actualizar(Long id, JugadorJovenDTO dto);

    void eliminar(Long id);

    List<JugadorJovenDTO> buscarPorUsuario(Long idUsuario);

    List<JugadorJovenDTO> buscarPorNivelHabilidad(String nivel);
}
