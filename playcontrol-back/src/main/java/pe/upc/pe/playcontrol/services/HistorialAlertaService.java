package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.HistorialAlertaDTO;

import java.util.List;

public interface HistorialAlertaService {
    List<HistorialAlertaDTO> listar();
    HistorialAlertaDTO buscarPorId(Long id);
    HistorialAlertaDTO insertar(HistorialAlertaDTO dto);
    HistorialAlertaDTO actualizar(Long id, HistorialAlertaDTO dto);
    void eliminar(Long id);
    List<HistorialAlertaDTO> buscarPorAlerta(Long idAlerta);
    List<HistorialAlertaDTO> buscarPorUsuario(Long idUsuario);
}
