package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.ProfesionalSalud;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesionalSaludRepository extends JpaRepository<ProfesionalSalud, Long> {
    List<ProfesionalSalud> findByEstado(String estado);

    Optional<ProfesionalSalud> findByUsuario_IdUsuario(Long idUsuario);
}
