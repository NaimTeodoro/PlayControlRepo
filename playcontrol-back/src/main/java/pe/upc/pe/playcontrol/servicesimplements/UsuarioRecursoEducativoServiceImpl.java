package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.UsuarioRecursoEducativoDTO;
import pe.upc.pe.playcontrol.entities.RecursoEducativo;
import pe.upc.pe.playcontrol.entities.UsuarioRecursoEducativo;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.RecursoEducativoRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.repositories.UsuarioRecursoEducativoRepository;
import pe.upc.pe.playcontrol.services.UsuarioRecursoEducativoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioRecursoEducativoServiceImpl implements UsuarioRecursoEducativoService {

    @Autowired
    private UsuarioRecursoEducativoRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecursoEducativoRepository recursoRepository;
    @Autowired
    private ModelMapper modelMapper;

    private UsuarioRecursoEducativoDTO toDTO(UsuarioRecursoEducativo e) {
        UsuarioRecursoEducativoDTO dto = modelMapper.map(e, UsuarioRecursoEducativoDTO.class);
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        dto.setIdRecurso(e.getRecurso().getIdRecurso());
        dto.setTituloRecurso(e.getRecurso().getTitulo());
        return dto;
    }

    private UsuarioRecursoEducativo toEntity(UsuarioRecursoEducativoDTO dto) {
        UsuarioRecursoEducativo e = modelMapper.map(dto, UsuarioRecursoEducativo.class);
        e.setUsuario(userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario())));
        e.setRecurso(recursoRepository.findById(dto.getIdRecurso())
                .orElseThrow(() -> new ResourceNotFoundException("RecursoEducativo", dto.getIdRecurso())));
        return e;
    }

    private UsuarioRecursoEducativo buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("UsuarioRecursoEducativo", id));
    }

    /** valoracion del recurso = promedio de las valoraciones de usuario (2 decimales). */
    private void recalcularValoracion(RecursoEducativo recurso) {
        double promedio = repository.findByRecurso_IdRecurso(recurso.getIdRecurso()).stream()
                .map(UsuarioRecursoEducativo::getValoracionUsuario)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        recurso.setValoracion(BigDecimal.valueOf(promedio).setScale(2, RoundingMode.HALF_UP));
        recursoRepository.save(recurso);
    }

    @Override
    public List<UsuarioRecursoEducativoDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public UsuarioRecursoEducativoDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    @Transactional
    public UsuarioRecursoEducativoDTO insertar(UsuarioRecursoEducativoDTO dto) {
        dto.setIdUsuarioRecurso(null);
        if (repository.existsByUsuario_IdUsuarioAndRecurso_IdRecurso(dto.getIdUsuario(), dto.getIdRecurso())) {
            throw new ConflictException("El usuario ya tiene registrado este recurso; use PUT para actualizarlo");
        }
        UsuarioRecursoEducativo e = toEntity(dto);
        if (e.getFechaAcceso() == null) {
            e.setFechaAcceso(LocalDateTime.now());
        }
        if (e.getCompletado() == null) {
            e.setCompletado(false);
        }
        if (e.getTiempoVisualizacionMinutos() == null) {
            e.setTiempoVisualizacionMinutos(0);
        }
        e = repository.save(e);
        RecursoEducativo recurso = e.getRecurso();
        recurso.setVisitas((recurso.getVisitas() == null ? 0 : recurso.getVisitas()) + 1);
        recalcularValoracion(recurso);
        return toDTO(e);
    }

    @Override
    @Transactional
    public UsuarioRecursoEducativoDTO actualizar(Long id, UsuarioRecursoEducativoDTO dto) {
        UsuarioRecursoEducativo existente = buscar(id);
        boolean cambiaPar = !existente.getUsuario().getIdUsuario().equals(dto.getIdUsuario())
                || !existente.getRecurso().getIdRecurso().equals(dto.getIdRecurso());
        if (cambiaPar && repository.existsByUsuario_IdUsuarioAndRecurso_IdRecurso(dto.getIdUsuario(), dto.getIdRecurso())) {
            throw new ConflictException("El usuario ya tiene registrado este recurso");
        }
        UsuarioRecursoEducativo e = toEntity(dto);
        e.setIdUsuarioRecurso(id);
        if (e.getFechaAcceso() == null) {
            e.setFechaAcceso(existente.getFechaAcceso());
        }
        e = repository.save(e);
        recalcularValoracion(e.getRecurso());
        return toDTO(e);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("UsuarioRecursoEducativo", id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<UsuarioRecursoEducativoDTO> buscarPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    public List<UsuarioRecursoEducativoDTO> buscarPorRecurso(Long idRecurso) {
        return repository.findByRecurso_IdRecurso(idRecurso).stream().map(this::toDTO).toList();
    }
}
