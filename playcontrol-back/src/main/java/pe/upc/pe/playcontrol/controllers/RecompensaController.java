package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.PuntosDTO;
import pe.upc.pe.playcontrol.dtos.RecompensaDTO;
import pe.upc.pe.playcontrol.services.RecompensaService;

import java.util.List;

/**
 * Logros de cada usuario (progreso, obtenidos y reclamados).
 */
@RestController
@RequestMapping("/api/recompensas")
public class RecompensaController {

    @Autowired
    private RecompensaService recompensaService;

    @GetMapping
    public List<RecompensaDTO> listar() {
        return recompensaService.listar();
    }

    @GetMapping("/{id}")
    public RecompensaDTO buscarPorId(@PathVariable Long id) {
        return recompensaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecompensaDTO insertar(@Valid @RequestBody RecompensaDTO dto) {
        return recompensaService.insertar(dto);
    }

    @PutMapping("/{id}")
    public RecompensaDTO actualizar(@PathVariable Long id, @Valid @RequestBody RecompensaDTO dto) {
        return recompensaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        recompensaService.eliminar(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<RecompensaDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return recompensaService.buscarPorUsuario(idUsuario);
    }

    /** {puntosTotales, logrosObtenidos, logrosEnProgreso} (suma puntosAsignados de OBTENIDA + RECLAMADA). */
    @GetMapping("/usuario/{idUsuario}/puntos")
    public PuntosDTO puntos(@PathVariable Long idUsuario) {
        return recompensaService.puntos(idUsuario);
    }

    /** Solo si esta OBTENIDA: pasa a RECLAMADA (fechaReclamada = ahora). Si no, 409. */
    @PutMapping("/{id}/reclamar")
    public RecompensaDTO reclamar(@PathVariable Long id) {
        return recompensaService.reclamar(id);
    }
}
