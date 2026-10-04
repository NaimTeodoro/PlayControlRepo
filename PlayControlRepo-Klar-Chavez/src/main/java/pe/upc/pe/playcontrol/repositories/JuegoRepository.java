package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.Juego;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    // Query nativo en SQL de PostgreSQL
    @Query(value = "SELECT * FROM videojuegos WHERE genero = :genero AND activo = true", nativeQuery = true)
    List<Juego> buscarPorGeneroNativo(@Param("genero") String genero);

    // Query nativo: juegos más jugados según cantidad de sesiones
    @Query(value = "SELECT j.* FROM videojuegos j " +
            "JOIN sesiones_juego s ON j.id_videojuego = s.id_videojuego " +
            "GROUP BY j.id_videojuego ORDER BY COUNT(s.id_sesion) DESC", nativeQuery = true)
    List<Juego> obtenerJuegosMasJugadosNativo();
}