package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.RecursoEducativo;

import java.util.List;

@Repository
public interface RecursoEducativoRepository extends JpaRepository<RecursoEducativo, Long> {

    List<RecursoEducativo> findByCategoriaIgnoreCase(String categoria);

    List<RecursoEducativo> findByAudienciaObjetivoIgnoreCase(String audienciaObjetivo);
}
