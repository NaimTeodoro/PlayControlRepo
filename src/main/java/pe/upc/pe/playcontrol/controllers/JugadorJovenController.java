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
import pe.upc.pe.playcontrol.dtos.JugadorJovenDTO;
import pe.upc.pe.playcontrol.services.JugadorJovenService;

@RestController
@RequestMapping({"/api/jugadores-jovenes"})
public class JugadorJovenController {
    @Autowired
    private JugadorJovenService service;

    public JugadorJovenController() {
    }

    @GetMapping
    public List<JugadorJovenDTO> listar() {
        return this.service.listar();
    }

    @GetMapping({"/{id}"})
    public JugadorJovenDTO buscarPorId(@PathVariable Long id) {
        return this.service.buscarPorId(id);
    }

    @PostMapping
    public JugadorJovenDTO insertar(@RequestBody JugadorJovenDTO dto) {
        return this.service.insertar(dto);
    }

    @PutMapping({"/{id}"})
    public JugadorJovenDTO actualizar(@PathVariable Long id, @RequestBody JugadorJovenDTO dto) {
        return this.service.actualizar(id, dto);
    }

    @DeleteMapping({"/{id}"})
    public void eliminar(@PathVariable Long id) {
        this.service.eliminar(id);
    }

    @GetMapping({"/usuario/{idUsuario}"})
    public List<JugadorJovenDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return this.service.buscarPorUsuario(idUsuario);
    }

    @GetMapping({"/nivel/{nivel}"})
    public List<JugadorJovenDTO> buscarPorNivel(@PathVariable String nivel) {
        return this.service.buscarPorNivelHabilidad(nivel);
    }
}
