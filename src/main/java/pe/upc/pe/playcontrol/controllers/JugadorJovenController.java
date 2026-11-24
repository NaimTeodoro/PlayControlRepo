package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.JugadorJovenDTO;
import pe.upc.pe.playcontrol.services.JugadorJovenService;

import java.util.List;

@RestController
@RequestMapping("/api/jugadores-jovenes")
public class JugadorJovenController {

    @Autowired
    private JugadorJovenService service;

    @GetMapping
    public List<JugadorJovenDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public JugadorJovenDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JugadorJovenDTO insertar(@Valid @RequestBody JugadorJovenDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public JugadorJovenDTO actualizar(@PathVariable Long id, @Valid @RequestBody JugadorJovenDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    /** Perfil de jugador de un usuario (uno; 404 si no existe). */
    @GetMapping("/usuario/{idUsuario}")
    public JugadorJovenDTO buscarPorUsuario(@PathVariable Long idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }

    /** Jugador por codigo de vinculacion (PC-XXXXXX). */
    @GetMapping("/codigo/{codigo}")
    public JugadorJovenDTO buscarPorCodigo(@PathVariable String codigo) {
        return service.buscarPorCodigo(codigo);
    }

    @GetMapping("/nivel/{nivel}")
    public List<JugadorJovenDTO> buscarPorNivel(@PathVariable String nivel) {
        return service.buscarPorNivelHabilidad(nivel);
    }
}
