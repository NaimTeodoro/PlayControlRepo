package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.DispositivoDTO;
import pe.upc.pe.playcontrol.services.DispositivoService;

import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {

    @Autowired
    private DispositivoService dispositivoService;

    @GetMapping
    public List<DispositivoDTO> listar() {
        return dispositivoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispositivoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(dispositivoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<DispositivoDTO> registrar(@RequestBody DispositivoDTO dto) {
        return ResponseEntity.ok(dispositivoService.registrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DispositivoDTO> actualizar(@PathVariable Long id, @RequestBody DispositivoDTO dto) {
        return ResponseEntity.ok(dispositivoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        dispositivoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<DispositivoDTO> listarPorUsuario(@PathVariable Long idUsuario) {
        return dispositivoService.listarPorUsuario(idUsuario);
    }

    @GetMapping("/buscar-os")
    public List<DispositivoDTO> buscarPorSistemaOperativo(@RequestParam String os) {
        return dispositivoService.buscarPorSistemaOperativo(os);
    }
}