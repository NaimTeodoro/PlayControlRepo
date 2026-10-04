package pe.upc.pe.playcontrol.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.RetoUsuario;
import java.util.List;

@Repository
public interface RetoUsuarioRepository extends JpaRepository<RetoUsuario, Long> {


    @Query(value = "SELECT * FROM retos_usuarios WHERE id_usuario = :idUsuario", nativeQuery = true)
    List<RetoUsuario> findRetosByUsuarioNativo(@Param("idUsuario") Long idUsuario);


    @Query(value = "SELECT COUNT(*) FROM retos_usuarios WHERE estado = 'COMPLETADO' AND id_usuario = :idUsuario", nativeQuery = true)
    Integer countRetosCompletadosByUsuarioNativo(@Param("idUsuario") Long idUsuario);
}