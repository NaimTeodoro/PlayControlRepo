package pe.upc.pe.playcontrol.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.dtos.PadreHijoDTO;
import pe.upc.pe.playcontrol.dtos.VincularDTO;
import pe.upc.pe.playcontrol.services.PadreHijoService;

import java.util.List;

@RestController
@RequestMapping("/api/padre-hijo")
public class PadreHijoController {

    @Autowired
    private PadreHijoService service;

    @GetMapping
    public List<PadreHijoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PadreHijoDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PadreHijoDTO insertar(@Valid @RequestBody PadreHijoDTO dto) {
        return service.insertar(dto);
    }

    @PutMapping("/{id}")
    public PadreHijoDTO actualizar(@PathVariable Long id, @Valid @RequestBody PadreHijoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    /** Hijos de un padre/tutor (con nombre, username y codigo de vinculacion del hijo). */
    @GetMapping("/padre/{idPadreTutor}")
    public List<PadreHijoDTO> buscarPorPadre(@PathVariable Long idPadreTutor) {
        return service.buscarPorPadre(idPadreTutor);
    }

    /** Padres/tutores vinculados a un jugador (el hijo ve aqui las solicitudes PENDIENTE). */
    @GetMapping("/joven/{idJugadorJoven}")
    public List<PadreHijoDTO> buscarPorJoven(@PathVariable Long idJugadorJoven) {
        return service.buscarPorJoven(idJugadorJoven);
    }

    /**
     * El padre ingresa el codigo de vinculacion del hijo. Crea el vinculo PENDIENTE (autorizado=false).
     * 404 si el codigo no existe; 409 si ya estan vinculados.
     */
    @PostMapping("/vincular")
    @ResponseStatus(HttpStatus.CREATED)
    public PadreHijoDTO vincular(@Valid @RequestBody VincularDTO dto) {
        return service.vincular(dto);
    }

    /** El hijo (dueño del vinculo PENDIENTE) o ADMIN autoriza el vinculo: estado ACTIVO, autorizado=true. El padre recibe 403. */
    @PutMapping("/{id}/autorizar")
    public PadreHijoDTO autorizar(@PathVariable Long id) {
        return service.autorizar(id);
    }
}
