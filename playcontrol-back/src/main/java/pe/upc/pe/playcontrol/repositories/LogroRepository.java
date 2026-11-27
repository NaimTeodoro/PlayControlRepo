package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.Logro;

@Repository
public interface LogroRepository extends JpaRepository<Logro, Long> {
}
