package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.ReportePadre;

import java.util.List;

@Repository
public interface ReportePadreRepository extends JpaRepository<ReportePadre, Long> {
    List<ReportePadre> findByPadreHijo_PadreTutor_IdPadreTutorOrderByFechaGeneracionDesc(Long idPadreTutor);

    List<ReportePadre> findByPadreHijo_IdPadreHijoOrderByFechaInicioPeriodoDesc(Long idPadreHijo);
}
