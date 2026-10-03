package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.Plataforma;

@Repository
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {}