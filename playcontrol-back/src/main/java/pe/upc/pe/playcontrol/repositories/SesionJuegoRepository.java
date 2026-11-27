package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.SesionJuego;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SesionJuegoRepository extends JpaRepository<SesionJuego, Long> {

    // Query nativo para obtener sesiones por usuario (mas recientes primero)
    @Query(value = "SELECT * FROM sesion_juego WHERE id_usuario = :idUsuario ORDER BY fecha_inicio DESC", nativeQuery = true)
    List<SesionJuego> listarPorUsuarioNativo(@Param("idUsuario") Long idUsuario);

    // Sesiones de un usuario iniciadas en [desde, hasta) (resumen y reportes)
    @Query("SELECT s FROM SesionJuego s WHERE s.usuario.idUsuario = :idUsuario " +
            "AND s.fechaInicio >= :desde AND s.fechaInicio < :hasta ORDER BY s.fechaInicio")
    List<SesionJuego> buscarPorUsuarioEntre(@Param("idUsuario") Long idUsuario,
                                            @Param("desde") LocalDateTime desde,
                                            @Param("hasta") LocalDateTime hasta);

    List<SesionJuego> findByUsuario_IdUsuarioAndEstadoSesion(Long idUsuario, String estadoSesion);
}
