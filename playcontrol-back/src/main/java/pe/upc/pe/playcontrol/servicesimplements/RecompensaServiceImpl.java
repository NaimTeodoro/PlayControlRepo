package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.PuntosDTO;
import pe.upc.pe.playcontrol.dtos.RecompensaDTO;
import pe.upc.pe.playcontrol.entities.Logro;
import pe.upc.pe.playcontrol.entities.Recompensa;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.LogroRepository;
import pe.upc.pe.playcontrol.repositories.RecompensaRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.RecompensaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecompensaServiceImpl implements RecompensaService {

    public static final String EN_PROGRESO = "EN_PROGRESO";
    public static final String OBTENIDA = "OBTENIDA";
    public static final String RECLAMADA = "RECLAMADA";

    @Autowired
    private RecompensaRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogroRepository logroRepository;
    @Autowired
    private ModelMapper modelMapper;

    private RecompensaDTO toDTO(Recompensa e) {
        RecompensaDTO dto = modelMapper.map(e, RecompensaDTO.class);
        Logro l = e.getLogro();
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        dto.setIdLogro(l.getIdLogro());
        dto.setNombreLogro(l.getNombreLogro());
        dto.setDescripcionLogro(l.getDescripcion());
        dto.setPuntosAsignados(l.getPuntosAsignados());
        dto.setImagenUrl(l.getImagenUrl());
        return dto;
    }

    private Recompensa toEntity(RecompensaDTO dto) {
        Recompensa e = modelMapper.map(dto, Recompensa.class);
        e.setUsuario(userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario())));
        e.setLogro(logroRepository.findById(dto.getIdLogro())
                .orElseThrow(() -> new ResourceNotFoundException("Logro", dto.getIdLogro())));
        return e;
    }

    /** OBTENIDA => 100 % y fechaObtenida; RECLAMADA => ademas fechaReclamada. */
    private void normalizar(Recompensa e) {
        LocalDateTime ahora = LocalDateTime.now();
        if (e.getEstado() == null) {
            e.setEstado(EN_PROGRESO);
        }
        if (e.getPorcentajeProgreso() == null) {
            e.setPorcentajeProgreso(0);
        }
        if (OBTENIDA.equals(e.getEstado()) || RECLAMADA.equals(e.getEstado())) {
            e.setPorcentajeProgreso(100);
            if (e.getFechaObtenida() == null) {
                e.setFechaObtenida(ahora);
            }
        }
        if (RECLAMADA.equals(e.getEstado()) && e.getFechaReclamada() == null) {
            e.setFechaReclamada(ahora);
        }
        if (e.getNotificacionEnviada() == null) {
            e.setNotificacionEnviada(false);
        }
    }

    private Recompensa buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recompensa", id));
    }

    @Override
    public List<RecompensaDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public RecompensaDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public RecompensaDTO insertar(RecompensaDTO dto) {
        dto.setIdRecompensa(null);
        if (repository.existsByUsuario_IdUsuarioAndLogro_IdLogro(dto.getIdUsuario(), dto.getIdLogro())) {
            throw new ConflictException("El usuario ya tiene registrado este logro");
        }
        Recompensa e = toEntity(dto);
        normalizar(e);
        return toDTO(repository.save(e));
    }

    @Override
    public RecompensaDTO actualizar(Long id, RecompensaDTO dto) {
        Recompensa existente = buscar(id);
        boolean cambiaPar = !existente.getUsuario().getIdUsuario().equals(dto.getIdUsuario())
                || !existente.getLogro().getIdLogro().equals(dto.getIdLogro());
        if (cambiaPar && repository.existsByUsuario_IdUsuarioAndLogro_IdLogro(dto.getIdUsuario(), dto.getIdLogro())) {
            throw new ConflictException("El usuario ya tiene registrado este logro");
        }
        Recompensa e = toEntity(dto);
        e.setIdRecompensa(id);
        if (e.getFechaObtenida() == null) {
            e.setFechaObtenida(existente.getFechaObtenida());
        }
        if (e.getFechaReclamada() == null) {
            e.setFechaReclamada(existente.getFechaReclamada());
        }
        normalizar(e);
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recompensa", id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<RecompensaDTO> buscarPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    public PuntosDTO puntos(Long idUsuario) {
        if (!userRepository.existsById(idUsuario)) {
            throw new ResourceNotFoundException("Usuario", idUsuario);
        }
        int puntos = 0;
        int obtenidos = 0;
        int enProgreso = 0;
        for (Recompensa r : repository.findByUsuario_IdUsuario(idUsuario)) {
            if (OBTENIDA.equals(r.getEstado()) || RECLAMADA.equals(r.getEstado())) {
                obtenidos++;
                puntos += r.getLogro().getPuntosAsignados() != null ? r.getLogro().getPuntosAsignados() : 0;
            } else if (EN_PROGRESO.equals(r.getEstado())) {
                enProgreso++;
            }
        }
        return new PuntosDTO(puntos, obtenidos, enProgreso);
    }

    @Override
    @Transactional
    public RecompensaDTO reclamar(Long id) {
        Recompensa e = buscar(id);
        if (!OBTENIDA.equals(e.getEstado())) {
            throw new ConflictException("Solo se puede reclamar un logro en estado OBTENIDA (estado actual: " + e.getEstado() + ")");
        }
        e.setEstado(RECLAMADA);
        e.setFechaReclamada(LocalDateTime.now());
        return toDTO(repository.save(e));
    }
}
