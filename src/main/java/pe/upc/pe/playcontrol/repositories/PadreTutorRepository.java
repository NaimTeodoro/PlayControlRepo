package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.PadreTutor;

import java.util.Optional;

@Repository
public interface PadreTutorRepository extends JpaRepository<PadreTutor, Long> {
    Optional<PadreTutor> findByUsuario_IdUsuario(Long idUsuario);
}
