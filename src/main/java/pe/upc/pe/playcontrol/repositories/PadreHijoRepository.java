package pe.upc.pe.playcontrol.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.PadreHijo;

@Repository
public interface PadreHijoRepository extends JpaRepository<PadreHijo, Long> {
    @Query(
            value = "SELECT * FROM padre_hijo WHERE id_padre_tutor = :idPadre",
            nativeQuery = true
    )
    List<PadreHijo> buscarHijosPorPadre(@Param("idPadre") Long idPadre);

    @Query(
            value = "SELECT * FROM padre_hijo WHERE id_jugador_joven = :idJoven AND estado = :estado",
            nativeQuery = true
    )
    List<PadreHijo> buscarPorJovenYEstado(@Param("idJoven") Long idJoven, @Param("estado") String estado);
}
