package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.ProfesionalClienteRequest;
import pe.upc.pe.playcontrol.dtos.ProfesionalClienteResponse;
import pe.upc.pe.playcontrol.services.ProfesionalClienteService;
import java.util.List;

@RestController
@RequestMapping("/api/profesional-clientes")
public class ProfesionalClienteController {

    private final ProfesionalClienteService service;

    public ProfesionalClienteController(ProfesionalClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProfesionalClienteResponse> listar(
            @RequestParam(name = "idProfesional", required = false) Integer idProfesional,
            @RequestParam(name = "idJugadorJoven", required = false) Integer idJugadorJoven) {
        return service.listar(idProfesional, idJugadorJoven);
    }

    @GetMapping("/{id}")
    public ProfesionalClienteResponse obtener(@PathVariable("id") Integer id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfesionalClienteResponse crear(@Valid @RequestBody ProfesionalClienteRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public ProfesionalClienteResponse actualizar(@PathVariable("id") Integer id,
                                                 @Valid @RequestBody ProfesionalClienteRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Integer id) {
        service.eliminar(id);
    }
}
