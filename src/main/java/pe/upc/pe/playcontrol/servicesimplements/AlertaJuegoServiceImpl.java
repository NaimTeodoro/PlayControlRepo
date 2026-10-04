package pe.upc.pe.playcontrol.servicesimplements;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.AlertaJuegoDTO;
import pe.upc.pe.playcontrol.entities.AlertaJuego;
import pe.upc.pe.playcontrol.entities.SessionJuego;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.AlertaJuegoRepository;
import pe.upc.pe.playcontrol.repositories.SessionJuegoRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.AlertaJuegoService;

@Service
public class AlertaJuegoServiceImpl implements AlertaJuegoService {
    @Autowired
    private AlertaJuegoRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionJuegoRepository sessionJuegoRepository;
    @Autowired
    private ModelMapper modelMapper;

    public AlertaJuegoServiceImpl() {
    }

    private AlertaJuegoDTO toDTO(AlertaJuego e) {
        AlertaJuegoDTO dto = (AlertaJuegoDTO)this.modelMapper.map(e, AlertaJuegoDTO.class);
        dto.setIdUsuario(e.getUser().getIdUsuario());
        dto.setIdSesion(e.getSesion() != null ? e.getSesion().getIdSesion() : null);
        return dto;
    }

    private AlertaJuego toEntity(AlertaJuegoDTO dto) {
        AlertaJuego e = (AlertaJuego)this.modelMapper.map(dto, AlertaJuego.class);
        User user = (User)this.userRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new RuntimeException("Sesion no encontrada"));
        e.setUser(user);
        if (dto.getIdSesion() != null) {
            SessionJuego sesion = (SessionJuego)this.sessionJuegoRepository.findById(dto.getIdSesion()).orElseThrow(() -> new RuntimeException("Sesion no encontrada"));
            e.setSesion(sesion);
        } else {
            e.setSesion((SessionJuego)null);
        }

        return e;
    }

    public List<AlertaJuegoDTO> listar() {
        return (List)this.repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AlertaJuegoDTO buscarPorId(Long id) {
        return this.toDTO((AlertaJuego)this.repository.findById(id).orElseThrow(() -> new RuntimeException("Alerta no encontrada")));
    }

    public AlertaJuegoDTO insertar(AlertaJuegoDTO dto) {
        dto.setIdAlerta((Long)null);
        AlertaJuego e = this.toEntity(dto);
        e.setFechaEnvio(LocalDateTime.now());
        if (e.getRespondidoUsuario() == null) {
            e.setRespondidoUsuario(false);
        }

        if (e.getEstadoAlerta() == null) {
            e.setEstadoAlerta("ENVIADA");
        }

        return this.toDTO((AlertaJuego)this.repository.save(e));
    }

    public AlertaJuegoDTO actualizar(Long id, AlertaJuegoDTO dto) {
        AlertaJuego existente = (AlertaJuego)this.repository.findById(id).orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
        AlertaJuego e = this.toEntity(dto);
        e.setIdAlerta(id);
        e.setFechaEnvio(existente.getFechaEnvio());
        return this.toDTO((AlertaJuego)this.repository.save(e));
    }

    public void eliminar(Long id) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("Alerta no encontrada");
        } else {
            this.repository.deleteById(id);
        }
    }

    public List<AlertaJuegoDTO> buscarPorUsuario(Long idUsuario) {
        return (List)this.repository.buscarPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AlertaJuegoDTO> buscarPendientesPorUsuario(Long idUsuario) {
        return (List)this.repository.buscarPendientesPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Long contarPorUsuarioYTipo(Long idUsuario, String tipo) {
        return this.repository.contarPorUsuarioYTipo(idUsuario, tipo);
    }
}
