package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public DispositivoDTO buscarPorId(@PathVariable Long id) {
        return dispositivoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DispositivoDTO insertar(@Valid @RequestBody DispositivoDTO dto) {
        return dispositivoService.insertar(dto);
    }

    @PutMapping("/{id}")
    public DispositivoDTO actualizar(@PathVariable Long id, @Valid @RequestBody DispositivoDTO dto) {
        return dispositivoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        dispositivoService.eliminar(id);
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
