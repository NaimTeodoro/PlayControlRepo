package pe.upc.pe.playcontrol.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dto.RecursoEducativoRequest;
import pe.upc.pe.playcontrol.dto.RecursoEducativoResponse;
import pe.upc.pe.playcontrol.service.RecursoEducativoService;

import java.util.List;

@RestController
@RequestMapping("/api/recursos-educativos")
public class RecursoEducativoController {

    private final RecursoEducativoService service;

    public RecursoEducativoController(RecursoEducativoService service) {
        this.service = service;
    }

    @GetMapping
    public List<RecursoEducativoResponse> listar(@RequestParam(name = "categoria", required = false) String categoria) {
        return service.listar(categoria);
    }

    @GetMapping("/{id}")
    public RecursoEducativoResponse obtener(@PathVariable("id") Integer id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecursoEducativoResponse crear(@Valid @RequestBody RecursoEducativoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public RecursoEducativoResponse actualizar(@PathVariable("id") Integer id,
                                               @Valid @RequestBody RecursoEducativoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Integer id) {
        service.eliminar(id);
    }
}
