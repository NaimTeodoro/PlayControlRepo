package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.ProfesionalSaludDTO;
import pe.upc.pe.playcontrol.services.ProfesionalSaludService;

import java.util.List;

@RestController
@RequestMapping("/api/profesionales-salud")
public class ProfesionalSaludController {

    @Autowired
    private ProfesionalSaludService service;

    /** GET /api/profesionales-salud?estado=ACTIVO (estado opcional). */
    @GetMapping
    public List<ProfesionalSaludDTO> listar(@RequestParam(name = "estado", required = false) String estado) {
        return service.listar(estado);
    }

    @GetMapping("/{id}")
    public ProfesionalSaludDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfesionalSaludDTO insertar(@Valid @RequestBody ProfesionalSaludDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public ProfesionalSaludDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProfesionalSaludDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    /** Perfil de profesional de un usuario (uno; 404 si no existe). */
    @GetMapping("/usuario/{idUsuario}")
    public ProfesionalSaludDTO buscarPorUsuario(@PathVariable Long idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }
}
