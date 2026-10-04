package pe.upc.pe.playcontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entity.RecursoEducativo;

import java.util.List;

@Repository
public interface RecursoEducativoRepository extends JpaRepository<RecursoEducativo, Integer> {

    List<RecursoEducativo> findByCategoriaIgnoreCase(String categoria);
}
