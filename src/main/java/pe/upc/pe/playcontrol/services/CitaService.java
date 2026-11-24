package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.CitaDTO;

import java.util.List;

/**
 * Citas con profesionales de salud (solo lectura). La cita es la proximaConsulta de una relacion
 * ProfesionalCliente con estado ACTIVO. Listas ordenadas por proximaConsulta ascendente, null al final.
 */
public interface CitaService {
    /** JUGADOR: las suyas. PADRE: las de todos sus hijos con vinculo activo. ADMIN: todas. */
    List<CitaDTO> misCitas();

    /**
     * Citas de un jugador (id de perfil). JUGADOR: solo el suyo. PADRE: solo hijos con vinculo activo.
     * ADMIN: cualquiera. 404 si el jugador no existe; 403 si no le pertenece.
     */
    List<CitaDTO> citasDeJugador(Long idJugadorJoven);
}
