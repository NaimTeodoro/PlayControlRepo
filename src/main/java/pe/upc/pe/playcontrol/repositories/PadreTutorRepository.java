
package pe.upc.pe.playcontrol.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.PadreTutor;

@Repository
public interface PadreTutorRepository extends JpaRepository<PadreTutor, Long> {
    @Query(
            value = "SELECT * FROM padre_tutor WHERE id_usuario = :idUsuario",
            nativeQuery = true
    )
    List<PadreTutor> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query(
            value = "SELECT * FROM padre_tutor WHERE nivel_tecnologia = :nivel",
            nativeQuery = true
    )
    List<PadreTutor> buscarPorNivelTecnologia(@Param("nivel") String nivel);
}
