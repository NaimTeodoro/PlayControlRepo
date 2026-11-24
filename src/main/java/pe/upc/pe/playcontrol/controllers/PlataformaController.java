package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.PlataformaDTO;
import pe.upc.pe.playcontrol.services.PlataformaService;

import java.util.List;

@RestController
@RequestMapping("/api/plataformas")
public class PlataformaController {

    @Autowired
    private PlataformaService plataformaService;

    @GetMapping
    public List<PlataformaDTO> listar() {
        return plataformaService.listar();
    }

    @GetMapping("/{id}")
    public PlataformaDTO buscarPorId(@PathVariable Long id) {
        return plataformaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlataformaDTO insertar(@Valid @RequestBody PlataformaDTO dto) {
        return plataformaService.insertar(dto);
    }

    @PutMapping("/{id}")
    public PlataformaDTO actualizar(@PathVariable Long id, @Valid @RequestBody PlataformaDTO dto) {
        return plataformaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        plataformaService.eliminar(id);
    }
}
