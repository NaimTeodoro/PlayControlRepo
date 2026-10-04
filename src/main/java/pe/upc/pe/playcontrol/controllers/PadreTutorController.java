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
import pe.upc.pe.playcontrol.dtos.PadreTutorDTO;
import pe.upc.pe.playcontrol.services.PadreTutorService;

@RestController
@RequestMapping({"/api/padres-tutores"})
public class PadreTutorController {
    @Autowired
    private PadreTutorService service;

    public PadreTutorController() {
    }

    @GetMapping
    public List<PadreTutorDTO> listar() {
        return this.service.listar();
    }

    @GetMapping({"/{id}"})
    public PadreTutorDTO buscarPorId(@PathVariable Long id) {
        return this.service.buscarPorId(id);
    }

    @PostMapping
    public PadreTutorDTO insertar(@RequestBody PadreTutorDTO dto) {
        return this.service.insertar(dto);
    }

    @PutMapping({"/{id}"})
    public PadreTutorDTO actualizar(@PathVariable Long id, @RequestBody PadreTutorDTO dto) {
        return this.service.actualizar(id, dto);
    }

    @DeleteMapping({"/{id}"})
    public void eliminar(@PathVariable Long id) {
        this.service.eliminar(id);
    }

    @GetMapping({"/usuario/{idUsuario}"})
    public List<PadreTutorDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return this.service.buscarPorUsuario(idUsuario);
    }

    @GetMapping({"/nivel-tecnologia/{nivel}"})
    public List<PadreTutorDTO> buscarPorNivelTecnologia(@PathVariable String nivel) {
        return this.service.buscarPorNivelTecnologia(nivel);
    }
}
