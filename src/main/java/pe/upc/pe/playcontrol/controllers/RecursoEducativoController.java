package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.RecursoEducativoDTO;
import pe.upc.pe.playcontrol.services.RecursoEducativoService;

import java.util.List;

@RestController
@RequestMapping("/api/recursos-educativos")
public class RecursoEducativoController {

    @Autowired
    private RecursoEducativoService service;

    /** Filtros opcionales: ?categoria=FAQ y/o ?audiencia=PADRES (sin distinguir mayusculas). */
    @GetMapping
    public List<RecursoEducativoDTO> listar(@RequestParam(name = "categoria", required = false) String categoria,
                                            @RequestParam(name = "audiencia", required = false) String audiencia) {
        return service.listar(categoria, audiencia);
    }

    @GetMapping("/{id}")
    public RecursoEducativoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecursoEducativoDTO insertar(@Valid @RequestBody RecursoEducativoDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public RecursoEducativoDTO actualizar(@PathVariable Long id, @Valid @RequestBody RecursoEducativoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
