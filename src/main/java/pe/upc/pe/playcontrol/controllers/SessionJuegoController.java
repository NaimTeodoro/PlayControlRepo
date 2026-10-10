package pe.upc.pe.playcontrol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.pe.playcontrol.entities.SessionJuego;
import pe.upc.pe.playcontrol.services.SessionJuegoService;

import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
@CrossOrigin(origins = "*")
public class SessionJuegoController {

    @Autowired
    private SessionJuegoService sesionJuegoService;

    @GetMapping
    public ResponseEntity<List<SessionJuego>> listar() {
        List<SessionJuego> sesiones = sesionJuegoService.listarSesiones();
        return new ResponseEntity<>(sesiones, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SessionJuego> registrar(@RequestBody SessionJuego sesionJuego) {
        SessionJuego nuevaSesion = sesionJuegoService.registrarSesion(sesionJuego);
        return new ResponseEntity<>(nuevaSesion, HttpStatus.CREATED);
    }
}