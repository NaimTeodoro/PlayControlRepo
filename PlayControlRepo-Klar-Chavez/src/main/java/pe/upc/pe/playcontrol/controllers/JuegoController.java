package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.JuegoDTO;
import pe.upc.pe.playcontrol.services.JuegoService;

import java.util.List;

@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @GetMapping
    public List<JuegoDTO> listar() {
        return juegoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(juegoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<JuegoDTO> registrar(@RequestBody JuegoDTO dto) {
        return ResponseEntity.ok(juegoService.registrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegoDTO> actualizar(@PathVariable Long id, @RequestBody JuegoDTO dto) {
        return ResponseEntity.ok(juegoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        juegoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints con Queries Nativos
    @GetMapping("/buscar-genero")
    public List<JuegoDTO> buscarPorGenero(@RequestParam String genero) {
        return juegoService.buscarPorGenero(genero);
    }

    @GetMapping("/mas-jugados")
    public List<JuegoDTO> obtenerMasJugados() {
        return juegoService.obtenerMasJugados();
    }
}
