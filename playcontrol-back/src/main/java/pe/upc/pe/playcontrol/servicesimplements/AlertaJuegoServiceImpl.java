package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.AlertaJuegoDTO;
import pe.upc.pe.playcontrol.dtos.ResponderAlertaDTO;
import pe.upc.pe.playcontrol.entities.AlertaJuego;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.AlertaJuegoRepository;
import pe.upc.pe.playcontrol.repositories.SesionJuegoRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.AlertaJuegoService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaJuegoServiceImpl implements AlertaJuegoService {

    @Autowired
    private AlertaJuegoRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SesionJuegoRepository sesionJuegoRepository;
    @Autowired
    private ModelMapper modelMapper;

    private AlertaJuegoDTO toDTO(AlertaJuego e) {
        AlertaJuegoDTO dto = modelMapper.map(e, AlertaJuegoDTO.class);
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        dto.setIdSesion(e.getSesion() != null ? e.getSesion().getIdSesion() : null);
        return dto;
    }

    private AlertaJuego toEntity(AlertaJuegoDTO dto) {
        AlertaJuego e = modelMapper.map(dto, AlertaJuego.class);
        e.setUsuario(userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario())));
        if (dto.getIdSesion() != null) {
            e.setSesion(sesionJuegoRepository.findById(dto.getIdSesion())
                    .orElseThrow(() -> new ResourceNotFoundException("SesionJuego", dto.getIdSesion())));
        } else {
            e.setSesion(null);
        }
        return e;
    }

    private AlertaJuego buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("AlertaJuego", id));
    }

    @Override
    public List<AlertaJuegoDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public AlertaJuegoDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public AlertaJuegoDTO insertar(AlertaJuegoDTO dto) {
        dto.setIdAlerta(null);
        AlertaJuego e = toEntity(dto);
        if (e.getFechaEnvio() == null) {
            e.setFechaEnvio(LocalDateTime.now());
        }
        if (e.getRespondidoUsuario() == null) {
            e.setRespondidoUsuario(false);
        }
        if (e.getEstadoAlerta() == null) {
            e.setEstadoAlerta("ENVIADA");
        }
        return toDTO(repository.save(e));
    }

    @Override
    public AlertaJuegoDTO actualizar(Long id, AlertaJuegoDTO dto) {
        AlertaJuego existente = buscar(id);
        AlertaJuego e = toEntity(dto);
        e.setIdAlerta(id);
        if (e.getFechaEnvio() == null) {
            e.setFechaEnvio(existente.getFechaEnvio());
        }
        if (e.getRespondidoUsuario() == null) {
            e.setRespondidoUsuario(existente.getRespondidoUsuario());
        }
        if (e.getEstadoAlerta() == null) {
            e.setEstadoAlerta(existente.getEstadoAlerta());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("AlertaJuego", id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<AlertaJuegoDTO> buscarPorUsuario(Long idUsuario) {
        return repository.buscarPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    public List<AlertaJuegoDTO> buscarPendientesPorUsuario(Long idUsuario) {
        return repository.buscarPendientesPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    public Long contarPorUsuarioYTipo(Long idUsuario, String tipo) {
        return repository.contarPorUsuarioYTipo(idUsuario, tipo);
    }

    @Override
    @Transactional
    public AlertaJuegoDTO responder(Long id, ResponderAlertaDTO dto) {
        AlertaJuego e = buscar(id);
        if (Boolean.TRUE.equals(e.getRespondidoUsuario())) {
            throw new ConflictException("La alerta ya fue respondida");
        }
        LocalDateTime ahora = LocalDateTime.now();
        e.setRespondidoUsuario(true);
        e.setRespuestaUsuario(dto.getRespuestaUsuario());
        e.setEstadoAlerta("RESPONDIDA");
        e.setTiempoRespuesta(e.getFechaEnvio() == null ? 0
                : (int) Math.max(0, Duration.between(e.getFechaEnvio(), ahora).toMinutes()));
        return toDTO(repository.save(e));
    }
}
