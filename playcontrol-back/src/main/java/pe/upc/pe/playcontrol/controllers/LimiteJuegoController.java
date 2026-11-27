package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.LimiteJuegoDTO;
import pe.upc.pe.playcontrol.services.LimiteJuegoService;
import pe.upc.pe.playcontrol.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/limites-juego")
public class LimiteJuegoController {

    @Autowired
    private LimiteJuegoService service;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<LimiteJuegoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public LimiteJuegoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /** Si no se envia idCreadoPor se usa el usuario autenticado. Al guardar uno ACTIVO, los demas pasan a INACTIVO. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LimiteJuegoDTO insertar(@Valid @RequestBody LimiteJuegoDTO dto, Authentication authentication) {
        completarCreadoPor(dto, authentication);
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public LimiteJuegoDTO actualizar(@PathVariable Long id, @Valid @RequestBody LimiteJuegoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<LimiteJuegoDTO> buscarPorUsuario(@PathVariable Long idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }

    @GetMapping("/usuario/{idUsuario}/activos")
    public List<LimiteJuegoDTO> buscarActivos(@PathVariable Long idUsuario) {
        return service.buscarActivosPorUsuario(idUsuario);
    }

    private void completarCreadoPor(LimiteJuegoDTO dto, Authentication authentication) {
        if (dto.getIdCreadoPor() == null && authentication != null) {
            dto.setIdCreadoPor(userService.obtenerActual(authentication.getName()).getIdUsuario());
        }
    }
}
