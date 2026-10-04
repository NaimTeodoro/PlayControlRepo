package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.RecompensaDTO;
import pe.upc.pe.playcontrol.services.RecompensaService;
import java.util.List;

@RestController
@RequestMapping("/api/recompensas")
public class RecompensaController {

    @Autowired
    private RecompensaService recompensaService;

    @PostMapping
    public ResponseEntity<RecompensaDTO> insert(@RequestBody RecompensaDTO recompensaDTO) {
        return new ResponseEntity<>(recompensaService.insert(recompensaDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecompensaDTO>> list() {
        return new ResponseEntity<>(recompensaService.list(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecompensaDTO> listId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recompensaService.listId(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        recompensaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}