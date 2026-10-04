package pe.upc.pe.playcontrol.services;

import java.util.List;
import pe.upc.pe.playcontrol.dtos.PadreTutorDTO;

public interface PadreTutorService {
    List<PadreTutorDTO> listar();

    PadreTutorDTO buscarPorId(Long id);

    PadreTutorDTO insertar(PadreTutorDTO dto);

    PadreTutorDTO actualizar(Long id, PadreTutorDTO dto);

    void eliminar(Long id);

    List<PadreTutorDTO> buscarPorUsuario(Long idUsuario);

    List<PadreTutorDTO> buscarPorNivelTecnologia(String nivel);
}
