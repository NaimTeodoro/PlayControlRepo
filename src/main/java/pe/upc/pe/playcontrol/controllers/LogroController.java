package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.LogroDTO;
import pe.upc.pe.playcontrol.services.LogroService;

import java.util.List;

/**
 * Catalogo de logros (lectura: cualquier autenticado; escritura: ADMIN).
 */
@RestController
@RequestMapping("/api/logros")
public class LogroController {

    @Autowired
    private LogroService service;

    @GetMapping
    public List<LogroDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public LogroDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogroDTO insertar(@Valid @RequestBody LogroDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public LogroDTO actualizar(@PathVariable Long id, @Valid @RequestBody LogroDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
