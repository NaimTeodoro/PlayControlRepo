package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.ProfesionalCliente;

import java.util.List;

@Repository
public interface ProfesionalClienteRepository extends JpaRepository<ProfesionalCliente, Integer> {

    List<ProfesionalCliente> findByProfesional_IdProfesional(Integer idProfesional);

    List<ProfesionalCliente> findByIdJugadorJoven(Integer idJugadorJoven);

    List<ProfesionalCliente> findByProfesional_IdProfesionalAndIdJugadorJoven(Integer idProfesional, Integer idJugadorJoven);
}
