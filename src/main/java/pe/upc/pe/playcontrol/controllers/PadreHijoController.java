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
import pe.upc.pe.playcontrol.dtos.PadreHijoDTO;
import pe.upc.pe.playcontrol.services.PadreHijoService;

@RestController
@RequestMapping({"/api/padre-hijo"})
public class PadreHijoController {
    @Autowired
    private PadreHijoService service;

    public PadreHijoController() {
    }

    @GetMapping
    public List<PadreHijoDTO> listar() {
        return this.service.listar();
    }

    @GetMapping({"/{id}"})
    public PadreHijoDTO buscarPorId(@PathVariable Long id) {
        return this.service.buscarPorId(id);
    }

    @PostMapping
    public PadreHijoDTO insertar(@RequestBody PadreHijoDTO dto) {
        return this.service.insertar(dto);
    }

    @PutMapping({"/{id}"})
    public PadreHijoDTO actualizar(@PathVariable Long id, @RequestBody PadreHijoDTO dto) {
        return this.service.actualizar(id, dto);
    }

    @DeleteMapping({"/{id}"})
    public void eliminar(@PathVariable Long id) {
        this.service.eliminar(id);
    }

    @GetMapping({"/padre/{idPadre}"})
    public List<PadreHijoDTO> buscarHijosPorPadre(@PathVariable Long idPadre) {
        return this.service.buscarHijosPorPadre(idPadre);
    }

    @GetMapping({"/joven/{idJoven}/estado/{estado}"})
    public List<PadreHijoDTO> buscarPorJovenYEstado(@PathVariable Long idJoven, @PathVariable String estado) {
        return this.service.buscarPorJovenYEstado(idJoven, estado);
    }
}
