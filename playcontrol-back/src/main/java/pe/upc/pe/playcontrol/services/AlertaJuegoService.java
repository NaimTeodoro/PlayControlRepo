package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.AlertaJuegoDTO;
import pe.upc.pe.playcontrol.dtos.ResponderAlertaDTO;

import java.util.List;

public interface AlertaJuegoService {
    List<AlertaJuegoDTO> listar();
    AlertaJuegoDTO buscarPorId(Long id);
    AlertaJuegoDTO insertar(AlertaJuegoDTO dto);
    AlertaJuegoDTO actualizar(Long id, AlertaJuegoDTO dto);
    void eliminar(Long id);
    List<AlertaJuegoDTO> buscarPorUsuario(Long idUsuario);
    List<AlertaJuegoDTO> buscarPendientesPorUsuario(Long idUsuario);
    Long contarPorUsuarioYTipo(Long idUsuario, String tipo);

    /** respondidoUsuario=true, estadoAlerta=RESPONDIDA, tiempoRespuesta = minutos desde fechaEnvio. 409 si ya fue respondida. */
    AlertaJuegoDTO responder(Long id, ResponderAlertaDTO dto);
}
