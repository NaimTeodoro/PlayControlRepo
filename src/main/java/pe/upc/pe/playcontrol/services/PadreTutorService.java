package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.PadreTutorDTO;

import java.util.List;

public interface PadreTutorService {
    List<PadreTutorDTO> listar();
    PadreTutorDTO buscarPorId(Long id);
    PadreTutorDTO insertar(PadreTutorDTO dto);
    PadreTutorDTO actualizar(Long id, PadreTutorDTO dto);
    void eliminar(Long id);
    PadreTutorDTO buscarPorUsuario(Long idUsuario);
}
