package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.JuegoDTO;
import java.util.List;

public interface JuegoService {
    JuegoDTO registrar(JuegoDTO dto);
    List<JuegoDTO> listar();
    JuegoDTO obtenerPorId(Long id);
    JuegoDTO actualizar(Long id, JuegoDTO dto);
    void eliminar(Long id);
    List<JuegoDTO> buscarPorGenero(String genero);
    List<JuegoDTO> obtenerMasJugados();
}
