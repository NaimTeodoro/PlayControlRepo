package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.entities.SessionJuego;
import java.util.List;

public interface SessionJuegoService {
    List<SessionJuego> listarSesiones();
    SessionJuego registrarSesion(SessionJuego sesionJuego);
}