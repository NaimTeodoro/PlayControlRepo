package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.RetoUsuarioDTO;
import java.util.List;

public interface RetoUsuarioService {
    RetoUsuarioDTO insert(RetoUsuarioDTO retoUsuarioDTO);
    List<RetoUsuarioDTO> list();
    List<RetoUsuarioDTO> listByUsuario(Long idUsuario);
    Integer countCompletados(Long idUsuario);
}