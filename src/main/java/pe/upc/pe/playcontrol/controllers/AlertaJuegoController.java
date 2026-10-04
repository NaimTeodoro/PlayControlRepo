package pe.upc.pe.playcontrol.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upc.pe.playcontrol.dtos.AlertaJuegoDTO;
import pe.upc.pe.playcontrol.services.AlertaJuegoService;

@RestController
@RequestMapping({"/api/alertas-juego"})
public class AlertaJuegoController {
    @Autowired
    private AlertaJuegoService service;

    public AlertaJuegoController() {
    }

    @GetMapping
    public List<AlertaJuegoDTO> listar() {
        return this.service.listar();
    }

    @GetMapping({"/{id}"})
    public AlertaJuegoDTO buscarPorId(@PathVariable Long id) {
        return this.service.buscarPorId(id);
    }

    @PostMapping
    public AlertaJuegoDTO insertar(@RequestBody AlertaJuegoDTO dto) {
        return this.service.insertar(dto);
    }

    @PutMapping({"/{id}"})
    public AlertaJuegoDTO actualizar(@PathVariable Long id, @RequestBody AlertaJuegoDTO dto) {
        return this.service.actualizar(id, dto);
    }

    @DeleteMapping({"/{id}"})
    public void eliminar(@PathVariable Long id) {
        this.service.eliminar(id);
    }

    @GetMapping({"/usuario/{idUsuario}"})
    public List<AlertaJuegoDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return this.service.buscarPorUsuario(idUsuario);
    }

    @GetMapping({"/usuario/{idUsuario}/pendientes"})
    public List<AlertaJuegoDTO> buscarPendientes(@PathVariable Long idUsuario) {
        return this.service.buscarPendientesPorUsuario(idUsuario);
    }

    @GetMapping({"/usuario/{idUsuario}/contar/{tipo}"})
    public Long contarPorTipo(@PathVariable Long idUsuario, @PathVariable String tipo) {
        return this.service.contarPorUsuarioYTipo(idUsuario, tipo);
    }
}
