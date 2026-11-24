package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.ReportePadreDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportePadreService {
    List<ReportePadreDTO> listar();
    ReportePadreDTO buscarPorId(Long id);
    ReportePadreDTO insertar(ReportePadreDTO dto);
    ReportePadreDTO actualizar(Long id, ReportePadreDTO dto);
    void eliminar(Long id);
    List<ReportePadreDTO> buscarPorPadre(Long idPadreTutor);
    List<ReportePadreDTO> buscarPorPadreHijo(Long idPadreHijo);

    /** Calcula y guarda el reporte de los ultimos 7 dias (SEMANAL) o 30 dias (MENSUAL), hoy incluido. */
    ReportePadreDTO generar(Long idPadreHijo, String periodo);

    /** Calcula y guarda el reporte de un periodo concreto [inicio, fin] (ambos incluidos). */
    ReportePadreDTO generarPeriodo(Long idPadreHijo, String periodo, LocalDate inicio, LocalDate fin);
}
