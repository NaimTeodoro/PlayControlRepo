package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.PadreTutorDTO;
import pe.upc.pe.playcontrol.services.PadreTutorService;

import java.util.List;

@RestController
@RequestMapping("/api/padres-tutores")
public class PadreTutorController {

    @Autowired
    private PadreTutorService service;

    @GetMapping
    public List<PadreTutorDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PadreTutorDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PadreTutorDTO insertar(@Valid @RequestBody PadreTutorDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public PadreTutorDTO actualizar(@PathVariable Long id, @Valid @RequestBody PadreTutorDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    /** Perfil de padre/tutor de un usuario (uno; 404 si no existe). */
    @GetMapping("/usuario/{idUsuario}")
    public PadreTutorDTO buscarPorUsuario(@PathVariable Long idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }
}
