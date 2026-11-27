package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.ProfesionalClienteDTO;

import java.util.List;

public interface ProfesionalClienteService {
    /** Filtros opcionales: idProfesional y/o idJugadorJoven. */
    List<ProfesionalClienteDTO> listar(Long idProfesional, Long idJugadorJoven);
    ProfesionalClienteDTO buscarPorId(Long id);
    ProfesionalClienteDTO insertar(ProfesionalClienteDTO dto);
    ProfesionalClienteDTO actualizar(Long id, ProfesionalClienteDTO dto);
    void eliminar(Long id);
    List<ProfesionalClienteDTO> buscarPorProfesional(Long idProfesional);
}
