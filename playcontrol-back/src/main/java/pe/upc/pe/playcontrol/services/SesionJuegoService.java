package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.ResumenSesionesDTO;
import pe.upc.pe.playcontrol.dtos.SesionJuegoDTO;

import java.util.List;

public interface SesionJuegoService {
    List<SesionJuegoDTO> listar();
    SesionJuegoDTO buscarPorId(Long id);
    SesionJuegoDTO insertar(SesionJuegoDTO dto);
    SesionJuegoDTO actualizar(Long id, SesionJuegoDTO dto);
    void eliminar(Long id);

    /** Sesiones del usuario ordenadas por fecha de inicio descendente. */
    List<SesionJuegoDTO> listarPorUsuario(Long idUsuario);

    /** Minutos de hoy, semana (lunes a hoy) y mes, frente al limite diario ACTIVO. */
    ResumenSesionesDTO resumen(Long idUsuario);
}
