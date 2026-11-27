package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.Recompensa;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
    List<Recompensa> findByUsuario_IdUsuario(Long idUsuario);

    boolean existsByUsuario_IdUsuarioAndLogro_IdLogro(Long idUsuario, Long idLogro);

    // Logros obtenidos (OBTENIDA o RECLAMADA) por un usuario en [desde, hasta) (reportes)
    @Query("SELECT r FROM Recompensa r WHERE r.usuario.idUsuario = :idUsuario " +
            "AND r.estado IN ('OBTENIDA', 'RECLAMADA') AND r.fechaObtenida >= :desde AND r.fechaObtenida < :hasta")
    List<Recompensa> buscarObtenidasEntre(@Param("idUsuario") Long idUsuario,
                                          @Param("desde") LocalDateTime desde,
                                          @Param("hasta") LocalDateTime hasta);
}
