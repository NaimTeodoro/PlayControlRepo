package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.ProfesionalCliente;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProfesionalClienteRepository extends JpaRepository<ProfesionalCliente, Long> {
    List<ProfesionalCliente> findByProfesional_IdProfesional(Long idProfesional);

    List<ProfesionalCliente> findByJugadorJoven_IdJugadorJoven(Long idJugadorJoven);

    List<ProfesionalCliente> findByProfesional_IdProfesionalAndJugadorJoven_IdJugadorJoven(Long idProfesional, Long idJugadorJoven);

    /** Citas (relaciones ACTIVO) de varios jugadores, la mas proxima primero. No llamar con coleccion vacia. */
    @Query(value = "SELECT * FROM profesional_cliente WHERE id_jugador_joven IN (:ids) AND estado = 'ACTIVO' " +
                   "ORDER BY proxima_consulta ASC NULLS LAST", nativeQuery = true)
    List<ProfesionalCliente> buscarCitasActivasPorJovenes(@Param("ids") Collection<Long> idsJugadorJoven);

    /** Todas las citas (relaciones ACTIVO), la mas proxima primero. Uso: ADMIN. */
    @Query(value = "SELECT * FROM profesional_cliente WHERE estado = 'ACTIVO' " +
                   "ORDER BY proxima_consulta ASC NULLS LAST", nativeQuery = true)
    List<ProfesionalCliente> buscarCitasActivas();
}
