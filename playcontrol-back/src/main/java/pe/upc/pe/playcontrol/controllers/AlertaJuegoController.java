package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.AlertaJuegoDTO;
import pe.upc.pe.playcontrol.dtos.ResponderAlertaDTO;
import pe.upc.pe.playcontrol.services.AlertaJuegoService;

import java.util.List;

@RestController
@RequestMapping("/api/alertas-juego")
public class AlertaJuegoController {

    @Autowired
    private AlertaJuegoService service;

    @GetMapping
    public List<AlertaJuegoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public AlertaJuegoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlertaJuegoDTO insertar(@Valid @RequestBody AlertaJuegoDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public AlertaJuegoDTO actualizar(@PathVariable Long id, @Valid @RequestBody AlertaJuegoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<AlertaJuegoDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }

    /** Alertas sin responder (respondidoUsuario = false). */
    @GetMapping("/usuario/{idUsuario}/pendientes")
    public List<AlertaJuegoDTO> buscarPendientes(@PathVariable Long idUsuario) {
        return service.buscarPendientesPorUsuario(idUsuario);
    }

    @GetMapping("/usuario/{idUsuario}/contar/{tipo}")
    public Long contarPorTipo(@PathVariable Long idUsuario, @PathVariable String tipo) {
        return service.contarPorUsuarioYTipo(idUsuario, tipo);
    }

    /** Body {respuestaUsuario}. Marca RESPONDIDA y calcula tiempoRespuesta (minutos desde fechaEnvio). 409 si ya fue respondida. */
    @PutMapping("/{id}/responder")
    public AlertaJuegoDTO responder(@PathVariable Long id, @Valid @RequestBody ResponderAlertaDTO dto) {
        return service.responder(id, dto);
    }
}
