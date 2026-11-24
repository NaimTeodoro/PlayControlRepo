package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.HistorialAlertaDTO;
import pe.upc.pe.playcontrol.services.HistorialAlertaService;

import java.util.List;

@RestController
@RequestMapping("/api/historial-alertas")
public class HistorialAlertaController {

    @Autowired
    private HistorialAlertaService service;

    @GetMapping
    public List<HistorialAlertaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public HistorialAlertaDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HistorialAlertaDTO insertar(@Valid @RequestBody HistorialAlertaDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public HistorialAlertaDTO actualizar(@PathVariable Long id, @Valid @RequestBody HistorialAlertaDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/alerta/{idAlerta}")
    public List<HistorialAlertaDTO> buscarPorAlerta(@PathVariable Long idAlerta) {
        return service.buscarPorAlerta(idAlerta);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<HistorialAlertaDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }
}
