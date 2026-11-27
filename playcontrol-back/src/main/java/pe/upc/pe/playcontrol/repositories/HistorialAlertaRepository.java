package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.HistorialAlerta;

import java.util.List;

@Repository
public interface HistorialAlertaRepository extends JpaRepository<HistorialAlerta, Long> {
    List<HistorialAlerta> findByAlerta_IdAlertaOrderByFechaDesc(Long idAlerta);

    // El usuario se obtiene a traves de la alerta (no se duplica en historial_alerta)
    @Query("SELECT h FROM HistorialAlerta h WHERE h.alerta.usuario.idUsuario = :idUsuario ORDER BY h.fecha DESC")
    List<HistorialAlerta> buscarPorUsuario(@Param("idUsuario") Long idUsuario);
}
