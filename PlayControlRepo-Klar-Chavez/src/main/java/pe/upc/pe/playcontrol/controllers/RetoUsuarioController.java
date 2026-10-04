package pe.upc.pe.playcontrol.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.RetoUsuarioDTO;
import pe.upc.pe.playcontrol.services.RetoUsuarioService;
import java.util.List;

@RestController
@RequestMapping("/api/retos-usuarios")
public class RetoUsuarioController {

    @Autowired
    private RetoUsuarioService retoUsuarioService;

    @PostMapping
    public ResponseEntity<RetoUsuarioDTO> insert(@RequestBody RetoUsuarioDTO retoUsuarioDTO) {
        return new ResponseEntity<>(retoUsuarioService.insert(retoUsuarioDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RetoUsuarioDTO>> list() {
        return new ResponseEntity<>(retoUsuarioService.list(), HttpStatus.OK);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<RetoUsuarioDTO>> listByUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return new ResponseEntity<>(retoUsuarioService.listByUsuario(idUsuario), HttpStatus.OK);
    }

    @GetMapping("/usuario/{idUsuario}/completados/count")
    public ResponseEntity<Integer> countCompletadosByUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return new ResponseEntity<>(retoUsuarioService.countCompletados(idUsuario), HttpStatus.OK);
    }
}
