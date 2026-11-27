package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.VideojuegoDTO;

import java.util.List;

public interface VideojuegoService {
    List<VideojuegoDTO> listar();
    VideojuegoDTO buscarPorId(Long id);
    VideojuegoDTO insertar(VideojuegoDTO dto);
    VideojuegoDTO actualizar(Long id, VideojuegoDTO dto);
    void eliminar(Long id);
    List<VideojuegoDTO> buscarPorGenero(String genero);
    List<VideojuegoDTO> obtenerMasJugados();
}
