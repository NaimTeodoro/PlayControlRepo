package pe.upc.pe.playcontrol.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.AlertaJuego;

@Repository
public interface AlertaJuegoRepository extends JpaRepository<AlertaJuego, Long> {
    @Query(
            value = "SELECT * FROM alerta_juego WHERE id_usuario = :idUsuario ORDER BY fecha_envio DESC",
            nativeQuery = true
    )
    List<AlertaJuego> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(
            value = "SELECT * FROM alerta_juego WHERE id_usuario = :idUsuario AND respondido_usuario = false",
            nativeQuery = true
    )
    List<AlertaJuego> buscarPendientesPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(
            value = "SELECT COUNT(*) FROM alerta_juego WHERE id_usuario = :idUsuario AND tipo_alerta = :tipo",
            nativeQuery = true
    )
    Long contarPorUsuarioYTipo(@Param("idUsuario") Long idUsuario, @Param("tipo") String tipo);
}
