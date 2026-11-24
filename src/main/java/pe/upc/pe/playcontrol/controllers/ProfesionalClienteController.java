package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.ProfesionalClienteDTO;
import pe.upc.pe.playcontrol.services.ProfesionalClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/profesional-clientes")
public class ProfesionalClienteController {

    @Autowired
    private ProfesionalClienteService service;

    /** Filtros opcionales: ?idProfesional= y/o ?idJugadorJoven= */
    @GetMapping
    public List<ProfesionalClienteDTO> listar(
            @RequestParam(name = "idProfesional", required = false) Long idProfesional,
            @RequestParam(name = "idJugadorJoven", required = false) Long idJugadorJoven) {
        return service.listar(idProfesional, idJugadorJoven);
    }

    @GetMapping("/{id}")
    public ProfesionalClienteDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfesionalClienteDTO insertar(@Valid @RequestBody ProfesionalClienteDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public ProfesionalClienteDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProfesionalClienteDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    /** Pacientes de un profesional (con nombre y apellido del jugador). */
    @GetMapping("/profesional/{idProfesional}")
    public List<ProfesionalClienteDTO> buscarPorProfesional(@PathVariable Long idProfesional) {
        return service.buscarPorProfesional(idProfesional);
    }
}
