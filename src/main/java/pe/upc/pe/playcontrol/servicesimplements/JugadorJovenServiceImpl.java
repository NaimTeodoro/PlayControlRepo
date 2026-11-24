package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.JugadorJovenDTO;
import pe.upc.pe.playcontrol.entities.JugadorJoven;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.JugadorJovenRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.JugadorJovenService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JugadorJovenServiceImpl implements JugadorJovenService {

    // Sin 0/O ni 1/I para que el codigo sea facil de dictar
    private static final String ALFABETO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();

    @Autowired
    private JugadorJovenRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    private JugadorJovenDTO toDTO(JugadorJoven e) {
        JugadorJovenDTO dto = modelMapper.map(e, JugadorJovenDTO.class);
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        return dto;
    }

    private JugadorJoven toEntity(JugadorJovenDTO dto) {
        JugadorJoven e = modelMapper.map(dto, JugadorJoven.class);
        User usuario = userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario()));
        e.setUsuario(usuario);
        return e;
    }

    private JugadorJoven buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("JugadorJoven", id));
    }

    @Override
    public List<JugadorJovenDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public JugadorJovenDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public JugadorJovenDTO insertar(JugadorJovenDTO dto) {
        dto.setIdJugadorJoven(null);
        if (repository.findByUsuario_IdUsuario(dto.getIdUsuario()).isPresent()) {
            throw new ConflictException("El usuario ya tiene un perfil de jugador");
        }
        JugadorJoven e = toEntity(dto);
        if (e.getCodigoVinculacion() == null || e.getCodigoVinculacion().isBlank()) {
            e.setCodigoVinculacion(generarCodigoVinculacion());
        } else if (repository.existsByCodigoVinculacion(e.getCodigoVinculacion())) {
            throw new ConflictException("El código de vinculación ya existe");
        }
        if (e.getFechaRegistroSegmento() == null) {
            e.setFechaRegistroSegmento(LocalDateTime.now());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public JugadorJovenDTO actualizar(Long id, JugadorJovenDTO dto) {
        JugadorJoven existente = buscar(id);
        JugadorJoven e = toEntity(dto);
        e.setIdJugadorJoven(id);
        // El codigo de vinculacion no se cambia por PUT
        e.setCodigoVinculacion(existente.getCodigoVinculacion());
        if (e.getFechaRegistroSegmento() == null) {
            e.setFechaRegistroSegmento(existente.getFechaRegistroSegmento());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("JugadorJoven", id);
        }
        repository.deleteById(id);
    }

    @Override
    public JugadorJovenDTO buscarPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuario(idUsuario).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe perfil de jugador para el usuario " + idUsuario));
    }

    @Override
    public JugadorJovenDTO buscarPorCodigo(String codigoVinculacion) {
        return repository.findByCodigoVinculacion(codigoVinculacion.trim().toUpperCase()).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un jugador con el código " + codigoVinculacion));
    }

    @Override
    public List<JugadorJovenDTO> buscarPorNivelHabilidad(String nivel) {
        return repository.buscarPorNivelHabilidad(nivel).stream().map(this::toDTO).toList();
    }

    @Override
    public String generarCodigoVinculacion() {
        String codigo;
        do {
            StringBuilder sb = new StringBuilder("PC-");
            for (int i = 0; i < 6; i++) {
                sb.append(ALFABETO.charAt(random.nextInt(ALFABETO.length())));
            }
            codigo = sb.toString();
        } while (repository.existsByCodigoVinculacion(codigo));
        return codigo;
    }
}
