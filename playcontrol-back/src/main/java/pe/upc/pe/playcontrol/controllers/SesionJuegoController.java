package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.ResumenSesionesDTO;
import pe.upc.pe.playcontrol.dtos.SesionJuegoDTO;
import pe.upc.pe.playcontrol.services.SesionJuegoService;

import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SesionJuegoController {

    @Autowired
    private SesionJuegoService sesionJuegoService;

    @GetMapping
    public List<SesionJuegoDTO> listar() {
        return sesionJuegoService.listar();
    }

    @GetMapping("/{id}")
    public SesionJuegoDTO buscarPorId(@PathVariable Long id) {
        return sesionJuegoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SesionJuegoDTO insertar(@Valid @RequestBody SesionJuegoDTO dto) {
        return sesionJuegoService.insertar(dto);
    }

    @PutMapping("/{id}")
    public SesionJuegoDTO actualizar(@PathVariable Long id, @Valid @RequestBody SesionJuegoDTO dto) {
        return sesionJuegoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        sesionJuegoService.eliminar(id);
    }

    /** Sesiones del usuario, mas recientes primero. */
    @GetMapping("/usuario/{idUsuario}")
    public List<SesionJuegoDTO> listarPorUsuario(@PathVariable Long idUsuario) {
        return sesionJuegoService.listarPorUsuario(idUsuario);
    }

    /** {minutosHoy, minutosSemana, minutosMes, sesionesHoy, limiteDiarioMinutos, porcentajeHoy, sesionEnCurso} */
    @GetMapping("/usuario/{idUsuario}/resumen")
    public ResumenSesionesDTO resumen(@PathVariable Long idUsuario) {
        return sesionJuegoService.resumen(idUsuario);
    }
}
