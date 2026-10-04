package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.RetoDTO;
import java.util.List;

public interface RetoService {
    RetoDTO insert(RetoDTO retoDTO);
    List<RetoDTO> list();
    RetoDTO listId(Long id);
    void delete(Long id);
}