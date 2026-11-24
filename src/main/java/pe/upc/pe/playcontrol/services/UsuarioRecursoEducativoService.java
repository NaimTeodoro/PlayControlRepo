package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.UsuarioRecursoEducativoDTO;

import java.util.List;

public interface UsuarioRecursoEducativoService {
    List<UsuarioRecursoEducativoDTO> listar();
    UsuarioRecursoEducativoDTO buscarPorId(Long id);
    /** 409 si el usuario ya tiene registrado ese recurso. Suma una visita al recurso. */
    UsuarioRecursoEducativoDTO insertar(UsuarioRecursoEducativoDTO dto);
    UsuarioRecursoEducativoDTO actualizar(Long id, UsuarioRecursoEducativoDTO dto);
    void eliminar(Long id);
    List<UsuarioRecursoEducativoDTO> buscarPorUsuario(Long idUsuario);
    List<UsuarioRecursoEducativoDTO> buscarPorRecurso(Long idRecurso);
}
