package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.LimiteJuegoDTO;

import java.util.List;

public interface LimiteJuegoService {
    List<LimiteJuegoDTO> listar();
    LimiteJuegoDTO buscarPorId(Long id);
    /** Si el limite se guarda ACTIVO, los demas limites activos del mismo usuario pasan a INACTIVO. */
    LimiteJuegoDTO insertar(LimiteJuegoDTO dto);
    LimiteJuegoDTO actualizar(Long id, LimiteJuegoDTO dto);
    void eliminar(Long id);
    List<LimiteJuegoDTO> buscarPorUsuario(Long idUsuario);
    List<LimiteJuegoDTO> buscarActivosPorUsuario(Long idUsuario);
}
