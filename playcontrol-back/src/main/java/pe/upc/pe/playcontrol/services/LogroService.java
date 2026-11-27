package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.LogroDTO;

import java.util.List;

public interface LogroService {
    List<LogroDTO> listar();
    LogroDTO buscarPorId(Long id);
    LogroDTO insertar(LogroDTO dto);
    LogroDTO actualizar(Long id, LogroDTO dto);
    void eliminar(Long id);
}
