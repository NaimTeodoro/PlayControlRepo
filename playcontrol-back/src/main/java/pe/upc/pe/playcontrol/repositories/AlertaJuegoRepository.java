package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.AlertaJuego;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertaJuegoRepository extends JpaRepository<AlertaJuego, Long> {
    @Query(value = "SELECT * FROM alerta_juego WHERE id_usuario = :idUsuario ORDER BY fecha_envio DESC", nativeQuery = true)
    List<AlertaJuego> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT * FROM alerta_juego WHERE id_usuario = :idUsuario AND respondido_usuario = false " +
            "ORDER BY fecha_envio DESC", nativeQuery = true)
    List<AlertaJuego> buscarPendientesPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT COUNT(*) FROM alerta_juego WHERE id_usuario = :idUsuario AND tipo_alerta = :tipo", nativeQuery = true)
    Long contarPorUsuarioYTipo(@Param("idUsuario") Long idUsuario, @Param("tipo") String tipo);

    // Alertas de un usuario enviadas en [desde, hasta) (reportes)
    @Query("SELECT a FROM AlertaJuego a WHERE a.usuario.idUsuario = :idUsuario " +
            "AND a.fechaEnvio >= :desde AND a.fechaEnvio < :hasta ORDER BY a.fechaEnvio")
    List<AlertaJuego> buscarPorUsuarioEntre(@Param("idUsuario") Long idUsuario,
                                            @Param("desde") LocalDateTime desde,
                                            @Param("hasta") LocalDateTime hasta);
}
