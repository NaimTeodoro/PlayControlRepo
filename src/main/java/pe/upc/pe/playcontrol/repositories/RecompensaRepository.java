package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.Recompensa;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
}
