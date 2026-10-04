package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.RetoDTO;
import pe.upc.pe.playcontrol.services.RetoService;
import java.util.List;

@RestController
@RequestMapping("/api/retos")
public class RetoController {

    @Autowired
    private RetoService retoService;

    @PostMapping
    public ResponseEntity<RetoDTO> insert(@RequestBody RetoDTO retoDTO) {
        return new ResponseEntity<>(retoService.insert(retoDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RetoDTO>> list() {
        return new ResponseEntity<>(retoService.list(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RetoDTO> listId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(retoService.listId(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        retoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}