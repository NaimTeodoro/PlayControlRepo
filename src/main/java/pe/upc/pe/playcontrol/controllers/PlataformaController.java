package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.PlataformaDTO;
import pe.upc.pe.playcontrol.services.PlataformaService;

import java.util.List;

@RestController
@RequestMapping("/api/plataformas")
public class PlataformaController {

    @Autowired
    private PlataformaService plataformaService;

    @PostMapping
    public ResponseEntity<PlataformaDTO> registrar(@RequestBody PlataformaDTO request) {
        PlataformaDTO response = plataformaService.registrar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlataformaDTO>> listar() {
        List<PlataformaDTO> response = plataformaService.listar();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlataformaDTO> obtenerPorId(@PathVariable Long id) {
        PlataformaDTO response = plataformaService.obtenerPorId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlataformaDTO> actualizar(@PathVariable Long id, @RequestBody PlataformaDTO request) {
        request.setIdPlataforma(id);
        PlataformaDTO response = plataformaService.actualizar(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        plataformaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}