package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.RecursoEducativoDTO;

import java.util.List;

public interface RecursoEducativoService {
    /** Filtros opcionales (sin distinguir mayusculas): categoria y/o audiencia. */
    List<RecursoEducativoDTO> listar(String categoria, String audiencia);
    RecursoEducativoDTO buscarPorId(Long id);
    RecursoEducativoDTO insertar(RecursoEducativoDTO dto);
    RecursoEducativoDTO actualizar(Long id, RecursoEducativoDTO dto);
    void eliminar(Long id);
}
