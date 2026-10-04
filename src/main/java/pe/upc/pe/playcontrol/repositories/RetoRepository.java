package pe.upc.pe.playcontrol.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.Reto;

@Repository
public interface RetoRepository extends JpaRepository<Reto, Long> {
}