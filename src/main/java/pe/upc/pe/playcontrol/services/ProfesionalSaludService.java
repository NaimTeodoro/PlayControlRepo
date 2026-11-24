package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.ProfesionalSaludDTO;

import java.util.List;

public interface ProfesionalSaludService {
    /** estado opcional: si es null o vacio lista todos. */
    List<ProfesionalSaludDTO> listar(String estado);
    ProfesionalSaludDTO buscarPorId(Long id);
    ProfesionalSaludDTO insertar(ProfesionalSaludDTO dto);
    ProfesionalSaludDTO actualizar(Long id, ProfesionalSaludDTO dto);
    void eliminar(Long id);
    ProfesionalSaludDTO buscarPorUsuario(Long idUsuario);
}
