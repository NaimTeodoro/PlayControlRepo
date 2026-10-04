package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.ProfesionalSaludRequest;
import pe.upc.pe.playcontrol.dtos.ProfesionalSaludResponse;
import pe.upc.pe.playcontrol.services.ProfesionalSaludService;

import java.util.List;

@RestController
@RequestMapping("/api/profesionales-salud")
public class ProfesionalSaludController {

    private final ProfesionalSaludService service;

    public ProfesionalSaludController(ProfesionalSaludService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProfesionalSaludResponse> listar(@RequestParam(name = "estado", required = false) String estado) {
        return service.listar(estado);
    }

    @GetMapping("/{id}")
    public ProfesionalSaludResponse obtener(@PathVariable("id") Integer id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfesionalSaludResponse crear(@Valid @RequestBody ProfesionalSaludRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public ProfesionalSaludResponse actualizar(@PathVariable("id") Integer id,
                                               @Valid @RequestBody ProfesionalSaludRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Integer id) {
        service.eliminar(id);
    }
}
