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
import pe.upc.pe.playcontrol.dtos.LimiteJuegoDTO;
import pe.upc.pe.playcontrol.services.LimiteJuegoService;

@RestController
@RequestMapping({"/api/limites-juego"})
public class LimiteJuegoController {
    @Autowired
    private LimiteJuegoService service;

    public LimiteJuegoController() {
    }

    @GetMapping
    public List<LimiteJuegoDTO> listar() {
        return this.service.listar();
    }

    @GetMapping({"/{id}"})
    public LimiteJuegoDTO buscarPorId(@PathVariable Long id) {
        return this.service.buscarPorId(id);
    }

    @PostMapping
    public LimiteJuegoDTO insertar(@RequestBody LimiteJuegoDTO dto) {
        return this.service.insertar(dto);
    }

    @PutMapping({"/{id}"})
    public LimiteJuegoDTO actualizar(@PathVariable Long id, @RequestBody LimiteJuegoDTO dto) {
        return this.service.actualizar(id, dto);
    }

    @DeleteMapping({"/{id}"})
    public void eliminar(@PathVariable Long id) {
        this.service.eliminar(id);
    }

    @GetMapping({"/usuario/{idUsuario}"})
    public List<LimiteJuegoDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return this.service.buscarPorUsuario(idUsuario);
    }

    @GetMapping({"/usuario/{idUsuario}/activos"})
    public List<LimiteJuegoDTO> buscarActivos(@PathVariable Long idUsuario) {
        return this.service.buscarActivosPorUsuario(idUsuario);
    }
}
