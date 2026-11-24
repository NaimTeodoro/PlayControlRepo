package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.PlataformaDTO;

import java.util.List;

public interface PlataformaService {
    List<PlataformaDTO> listar();
    PlataformaDTO buscarPorId(Long id);
    PlataformaDTO insertar(PlataformaDTO dto);
    PlataformaDTO actualizar(Long id, PlataformaDTO dto);
    void eliminar(Long id);
}
