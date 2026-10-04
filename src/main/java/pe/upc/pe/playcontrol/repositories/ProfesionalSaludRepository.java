package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.ProfesionalSalud;

import java.util.List;

@Repository
public interface ProfesionalSaludRepository extends JpaRepository<ProfesionalSalud, Integer> {

    List<ProfesionalSalud> findByEstado(String estado);
}
