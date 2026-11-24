package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.DispositivoDTO;

import java.util.List;

public interface DispositivoService {
    List<DispositivoDTO> listar();
    DispositivoDTO buscarPorId(Long id);
    DispositivoDTO insertar(DispositivoDTO dto);
    DispositivoDTO actualizar(Long id, DispositivoDTO dto);
    void eliminar(Long id);
    List<DispositivoDTO> listarPorUsuario(Long idUsuario);
    List<DispositivoDTO> buscarPorSistemaOperativo(String os);
}
