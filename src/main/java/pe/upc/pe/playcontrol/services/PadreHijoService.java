package pe.upc.pe.playcontrol.services;

import java.util.List;
import pe.upc.pe.playcontrol.dtos.PadreHijoDTO;

public interface PadreHijoService {
    List<PadreHijoDTO> listar();

    PadreHijoDTO buscarPorId(Long id);

    PadreHijoDTO insertar(PadreHijoDTO dto);

    PadreHijoDTO actualizar(Long id, PadreHijoDTO dto);

    void eliminar(Long id);

    List<PadreHijoDTO> buscarHijosPorPadre(Long idPadre);

    List<PadreHijoDTO> buscarPorJovenYEstado(Long idJoven, String estado);
}
