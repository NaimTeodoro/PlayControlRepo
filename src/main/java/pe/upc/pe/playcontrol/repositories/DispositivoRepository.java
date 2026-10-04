package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.Dispositivo;
import java.util.List;

@Repository
public interface DispositivoRepository extends JpaRepository<Dispositivo, Long> {

    // Query nativo 1: Buscar dispositivos por usuario
    @Query(value = "SELECT * FROM dispositivos WHERE id_usuario = :idUsuario", nativeQuery = true)
    List<Dispositivo> listarPorUsuarioNativo(@Param("idUsuario") Long idUsuario);

    // Query nativo 2: Filtrar por sistema operativo
    @Query(value = "SELECT * FROM dispositivos WHERE sistema_operativo ILIKE %:os%", nativeQuery = true)
    List<Dispositivo> buscarPorSistemaOperativoNativo(@Param("os") String os);
}