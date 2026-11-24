package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.UsuarioRecursoEducativo;

import java.util.List;

@Repository
public interface UsuarioRecursoEducativoRepository extends JpaRepository<UsuarioRecursoEducativo, Long> {
    List<UsuarioRecursoEducativo> findByUsuario_IdUsuario(Long idUsuario);

    List<UsuarioRecursoEducativo> findByRecurso_IdRecurso(Long idRecurso);

    boolean existsByUsuario_IdUsuarioAndRecurso_IdRecurso(Long idUsuario, Long idRecurso);
}
