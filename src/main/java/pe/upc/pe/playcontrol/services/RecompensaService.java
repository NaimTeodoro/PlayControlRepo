package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.PuntosDTO;
import pe.upc.pe.playcontrol.dtos.RecompensaDTO;

import java.util.List;

public interface RecompensaService {
    List<RecompensaDTO> listar();
    RecompensaDTO buscarPorId(Long id);
    RecompensaDTO insertar(RecompensaDTO dto);
    RecompensaDTO actualizar(Long id, RecompensaDTO dto);
    void eliminar(Long id);
    List<RecompensaDTO> buscarPorUsuario(Long idUsuario);

    /** Suma de puntosAsignados de los logros OBTENIDA + RECLAMADA del usuario. */
    PuntosDTO puntos(Long idUsuario);

    /** Solo si esta OBTENIDA: pasa a RECLAMADA con fechaReclamada = ahora. Si no, 409. */
    RecompensaDTO reclamar(Long id);
}
