package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.PlataformaDTO;
import java.util.List;

public interface PlataformaService {
    PlataformaDTO registrar(PlataformaDTO request);
    List<PlataformaDTO> listar();
    PlataformaDTO obtenerPorId(Long id);
    PlataformaDTO actualizar(PlataformaDTO request);
    void eliminar(Long id);
}