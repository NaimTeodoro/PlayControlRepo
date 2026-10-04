package pe.upc.pe.playcontrol.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.JugadorJoven;

@Repository
public interface JugadorJovenRepository extends JpaRepository<JugadorJoven, Long> {
    @Query(
            value = "SELECT * FROM jugador_joven WHERE id_usuario = :idUsuario",
            nativeQuery = true
    )
    List<JugadorJoven> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(
            value = "SELECT * FROM jugador_joven WHERE nivel_habilidad = :nivel",
            nativeQuery = true
    )
    List<JugadorJoven> buscarPorNivelHabilidad(@Param("nivel") String nivel);
}
