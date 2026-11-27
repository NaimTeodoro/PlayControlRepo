package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.ReportePadreDTO;
import pe.upc.pe.playcontrol.services.ReportePadreService;

import java.util.List;

@RestController
@RequestMapping("/api/reportes-padre")
public class ReportePadreController {

    @Autowired
    private ReportePadreService service;

    @GetMapping
    public List<ReportePadreDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ReportePadreDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReportePadreDTO insertar(@Valid @RequestBody ReportePadreDTO dto) {
        return service.insertar(dto);
    }

    /** Tambien sirve para marcar un reporte como visto (visto=true guarda fechaVisto). */
    @PutMapping("/{id}")
    public ReportePadreDTO actualizar(@PathVariable Long id, @Valid @RequestBody ReportePadreDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    /** Reportes de todos los hijos de un padre/tutor, mas recientes primero. */
    @GetMapping("/padre/{idPadreTutor}")
    public List<ReportePadreDTO> buscarPorPadre(@PathVariable Long idPadreTutor) {
        return service.buscarPorPadre(idPadreTutor);
    }

    @GetMapping("/padre-hijo/{idPadreHijo}")
    public List<ReportePadreDTO> buscarPorPadreHijo(@PathVariable Long idPadreHijo) {
        return service.buscarPorPadreHijo(idPadreHijo);
    }

    /**
     * Calcula y guarda el reporte de los ultimos 7 dias (SEMANAL) o 30 dias (MENSUAL) a partir de las sesiones,
     * alertas y recompensas reales del hijo. 409 si el vinculo no esta ACTIVO.
     */
    @PostMapping("/generar/{idPadreHijo}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReportePadreDTO generar(@PathVariable Long idPadreHijo,
                                   @RequestParam(name = "periodo", defaultValue = "SEMANAL") String periodo) {
        return service.generar(idPadreHijo, periodo);
    }
}
