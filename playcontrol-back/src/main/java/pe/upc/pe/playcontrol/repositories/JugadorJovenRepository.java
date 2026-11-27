package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.JugadorJoven;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorJovenRepository extends JpaRepository<JugadorJoven, Long> {
    Optional<JugadorJoven> findByUsuario_IdUsuario(Long idUsuario);

    Optional<JugadorJoven> findByCodigoVinculacion(String codigoVinculacion);

    boolean existsByCodigoVinculacion(String codigoVinculacion);

    @Query(value = "SELECT * FROM jugador_joven WHERE nivel_habilidad = :nivel", nativeQuery = true)
    List<JugadorJoven> buscarPorNivelHabilidad(@Param("nivel") String nivel);
}
