package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.CitaDTO;
import pe.upc.pe.playcontrol.services.CitaService;

import java.util.List;

/**
 * Citas con profesionales: SOLO LECTURA para jugador, padre y admin (sin datos clinicos).
 * Las fechas las sigue editando el profesional en PUT /api/profesional-clientes/{id}.
 * Ordenadas por proximaConsulta ascendente (null al final).
 */
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService service;

    /** JUGADOR: sus citas. PADRE: las de todos sus hijos con vinculo activo. ADMIN: todas. */
    @GetMapping("/mias")
    public List<CitaDTO> misCitas() {
        return service.misCitas();
    }

    /** Citas de un jugador concreto (id de perfil JugadorJoven). 403 si no es suyo / no es su hijo activo. */
    @GetMapping("/jugador/{idJugadorJoven}")
    public List<CitaDTO> citasDeJugador(@PathVariable Long idJugadorJoven) {
        return service.citasDeJugador(idJugadorJoven);
    }
}
