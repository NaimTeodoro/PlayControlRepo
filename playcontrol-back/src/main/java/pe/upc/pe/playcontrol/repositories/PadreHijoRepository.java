package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.PadreHijo;

import java.util.List;
import java.util.Optional;

@Repository
public interface PadreHijoRepository extends JpaRepository<PadreHijo, Long> {
    @Query(value = "SELECT * FROM padre_hijo WHERE id_padre_tutor = :idPadre", nativeQuery = true)
    List<PadreHijo> buscarHijosPorPadre(@Param("idPadre") Long idPadre);

    @Query(value = "SELECT * FROM padre_hijo WHERE id_jugador_joven = :idJoven", nativeQuery = true)
    List<PadreHijo> buscarPorJoven(@Param("idJoven") Long idJoven);

    @Query(value = "SELECT * FROM padre_hijo WHERE id_jugador_joven = :idJoven AND estado = :estado", nativeQuery = true)
    List<PadreHijo> buscarPorJovenYEstado(@Param("idJoven") Long idJoven, @Param("estado") String estado);

    boolean existsByPadreTutor_IdPadreTutorAndJugadorJoven_IdJugadorJoven(Long idPadreTutor, Long idJugadorJoven);

    /** El par (padre, joven) es unico (uk_padre_hijo). */
    Optional<PadreHijo> findByPadreTutor_IdPadreTutorAndJugadorJoven_IdJugadorJoven(Long idPadreTutor, Long idJugadorJoven);
}
