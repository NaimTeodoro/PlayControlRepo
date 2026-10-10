package pe.upc.pe.playcontrol.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.entities.SessionJuego;
import pe.upc.pe.playcontrol.repositories.SessionJuegoRepository;
import pe.upc.pe.playcontrol.services.SessionJuegoService;

import java.util.List;

@Service
public class SessionJuegoServiceImpl implements SessionJuegoService {

    @Autowired
    private SessionJuegoRepository sesionJuegoRepository;

    @Override
    public List<SessionJuego> listarSesiones() {
        return sesionJuegoRepository.findAll();
    }

    @Override
    public SessionJuego registrarSesion(SessionJuego sesionJuego) {
        return sesionJuegoRepository.save(sesionJuego);
    }
}