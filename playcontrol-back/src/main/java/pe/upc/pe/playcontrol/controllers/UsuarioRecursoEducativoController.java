package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.UsuarioRecursoEducativoDTO;
import pe.upc.pe.playcontrol.services.UsuarioRecursoEducativoService;

import java.util.List;

/**
 * Consumo de recursos educativos por usuario (visto, tiempo, completado, valoracion).
 */
@RestController
@RequestMapping("/api/usuario-recursos")
public class UsuarioRecursoEducativoController {

    @Autowired
    private UsuarioRecursoEducativoService service;

    @GetMapping
    public List<UsuarioRecursoEducativoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public UsuarioRecursoEducativoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /** 409 si el usuario ya registro ese recurso (usar PUT). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioRecursoEducativoDTO insertar(@Valid @RequestBody UsuarioRecursoEducativoDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public UsuarioRecursoEducativoDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRecursoEducativoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<UsuarioRecursoEducativoDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }

    @GetMapping("/recurso/{idRecurso}")
    public List<UsuarioRecursoEducativoDTO> buscarPorRecurso(@PathVariable Long idRecurso) {
        return service.buscarPorRecurso(idRecurso);
    }
}
