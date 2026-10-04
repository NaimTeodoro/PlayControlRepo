package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.DispositivoDTO;
import java.util.List;

public interface DispositivoService {
    DispositivoDTO registrar(DispositivoDTO dto);
    List<DispositivoDTO> listar();
    DispositivoDTO obtenerPorId(Long id);
    DispositivoDTO actualizar(Long id, DispositivoDTO dto);
    void eliminar(Long id);
    List<DispositivoDTO> listarPorUsuario(Long idUsuario);
    List<DispositivoDTO> buscarPorSistemaOperativo(String os);
}