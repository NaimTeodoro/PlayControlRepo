package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.SessionJuego;

import java.util.List;

@Repository
public interface SessionJuegoRepository extends JpaRepository<SessionJuego, Long> {

    // Query nativo para obtener sesiones por usuario
    @Query(value = "SELECT * FROM sesiones_juego WHERE id_usuario = :idUsuario ORDER BY fecha_inicio DESC", nativeQuery = true)
    List<SessionJuego> listarPorUsuarioNativo(@Param("idUsuario") Long idUsuario);
}