package pe.upc.pe.playcontrol.services;

import java.util.List;
import pe.upc.pe.playcontrol.dtos.AlertaJuegoDTO;

public interface AlertaJuegoService {
    List<AlertaJuegoDTO> listar();

    AlertaJuegoDTO buscarPorId(Long id);

    AlertaJuegoDTO insertar(AlertaJuegoDTO dto);

    AlertaJuegoDTO actualizar(Long id, AlertaJuegoDTO dto);

    void eliminar(Long id);

    List<AlertaJuegoDTO> buscarPorUsuario(Long idUsuario);

    List<AlertaJuegoDTO> buscarPendientesPorUsuario(Long idUsuario);

    Long contarPorUsuarioYTipo(Long idUsuario, String tipo);
}
