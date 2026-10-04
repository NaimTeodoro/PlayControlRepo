package pe.upc.pe.playcontrol.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.LimiteJuego;

@Repository
public interface LimiteJuegoRepository extends JpaRepository<LimiteJuego, Long> {
    @Query(
            value = "SELECT * FROM limite_juego WHERE id_usuario = :idUsuario",
            nativeQuery = true
    )
    List<LimiteJuego> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(
            value = "SELECT * FROM limite_juego WHERE id_usuario = :idUsuario AND estado = 'ACTIVO'",
            nativeQuery = true
    )
    List<LimiteJuego> buscarActivosPorUsuario(@Param("idUsuario") Long idUsuario);
}
