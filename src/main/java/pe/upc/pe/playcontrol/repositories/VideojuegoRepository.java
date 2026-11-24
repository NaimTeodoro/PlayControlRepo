package pe.upc.pe.playcontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upc.pe.playcontrol.entities.Videojuego;

import java.util.List;

@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {

    // Query nativo en SQL de PostgreSQL
    @Query(value = "SELECT * FROM videojuego WHERE genero = :genero AND activo = true", nativeQuery = true)
    List<Videojuego> buscarPorGeneroNativo(@Param("genero") String genero);

    // Query nativo: juegos mas jugados segun cantidad de sesiones
    @Query(value = "SELECT j.* FROM videojuego j " +
            "JOIN sesion_juego s ON j.id_videojuego = s.id_videojuego " +
            "GROUP BY j.id_videojuego ORDER BY COUNT(s.id_sesion) DESC", nativeQuery = true)
    List<Videojuego> obtenerJuegosMasJugadosNativo();
}
