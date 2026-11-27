package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.VideojuegoDTO;
import pe.upc.pe.playcontrol.services.VideojuegoService;

import java.util.List;

/**
 * Catalogo de videojuegos (antes /api/juegos).
 */
@RestController
@RequestMapping("/api/videojuegos")
public class VideojuegoController {

    @Autowired
    private VideojuegoService videojuegoService;

    @GetMapping
    public List<VideojuegoDTO> listar() {
        return videojuegoService.listar();
    }

    @GetMapping("/{id}")
    public VideojuegoDTO buscarPorId(@PathVariable Long id) {
        return videojuegoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VideojuegoDTO insertar(@Valid @RequestBody VideojuegoDTO dto) {
        return videojuegoService.insertar(dto);
    }

    @PutMapping("/{id}")
    public VideojuegoDTO actualizar(@PathVariable Long id, @Valid @RequestBody VideojuegoDTO dto) {
        return videojuegoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        videojuegoService.eliminar(id);
    }

    // Endpoints con queries nativos
    @GetMapping("/buscar-genero")
    public List<VideojuegoDTO> buscarPorGenero(@RequestParam String genero) {
        return videojuegoService.buscarPorGenero(genero);
    }

    @GetMapping("/mas-jugados")
    public List<VideojuegoDTO> obtenerMasJugados() {
        return videojuegoService.obtenerMasJugados();
    }
}
