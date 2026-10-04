package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.RecompensaDTO;
import java.util.List;

public interface RecompensaService {
    RecompensaDTO insert(RecompensaDTO recompensaDTO);
    List<RecompensaDTO> list();
    RecompensaDTO listId(Long id);
    void delete(Long id);
}