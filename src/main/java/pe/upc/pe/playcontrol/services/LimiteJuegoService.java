package pe.upc.pe.playcontrol.services;

import java.util.List;
import pe.upc.pe.playcontrol.dtos.LimiteJuegoDTO;

public interface LimiteJuegoService {
    List<LimiteJuegoDTO> listar();

    LimiteJuegoDTO buscarPorId(Long id);

    LimiteJuegoDTO insertar(LimiteJuegoDTO dto);

    LimiteJuegoDTO actualizar(Long id, LimiteJuegoDTO dto);

    void eliminar(Long id);

    List<LimiteJuegoDTO> buscarPorUsuario(Long idUsuario);

    List<LimiteJuegoDTO> buscarActivosPorUsuario(Long idUsuario);
}
