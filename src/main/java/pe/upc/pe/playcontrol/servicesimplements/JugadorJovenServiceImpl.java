package pe.upc.pe.playcontrol.servicesimplements;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.JugadorJovenDTO;
import pe.upc.pe.playcontrol.entities.JugadorJoven;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.JugadorJovenRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.JugadorJovenService;

@Service
public class JugadorJovenServiceImpl implements JugadorJovenService {
    @Autowired
    private JugadorJovenRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public JugadorJovenServiceImpl() {
    }

    private JugadorJovenDTO toDTO(JugadorJoven e) {
        JugadorJovenDTO dto = (JugadorJovenDTO)this.modelMapper.map(e, JugadorJovenDTO.class);
        dto.setIdUsuario(e.getUser().getIdUsuario());
        return dto;
    }

    private JugadorJoven toEntity(JugadorJovenDTO dto) {
        JugadorJoven e = (JugadorJoven)this.modelMapper.map(dto, JugadorJoven.class);
        User user = (User)this.userRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        e.setUser(user);
        return e;
    }

    public List<JugadorJovenDTO> listar() {
        return (List)this.repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public JugadorJovenDTO buscarPorId(Long id) {
        return this.toDTO((JugadorJoven)this.repository.findById(id).orElseThrow(() -> new RuntimeException("JugadorJoven no encontrado")));
    }

    public JugadorJovenDTO insertar(JugadorJovenDTO dto) {
        dto.setIdJugadorJoven((Long)null);
        JugadorJoven e = this.toEntity(dto);
        if (e.getFechaRegistroSegmento() == null) {
            e.setFechaRegistroSegmento(LocalDateTime.now());
        }

        return this.toDTO((JugadorJoven)this.repository.save(e));
    }

    public JugadorJovenDTO actualizar(Long id, JugadorJovenDTO dto) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("JugadorJoven no encontrado");
        } else {
            JugadorJoven e = this.toEntity(dto);
            e.setIdJugadorJoven(id);
            return this.toDTO((JugadorJoven)this.repository.save(e));
        }
    }

    public void eliminar(Long id) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("JugadorJoven no encontrado");
        } else {
            this.repository.deleteById(id);
        }
    }

    public List<JugadorJovenDTO> buscarPorUsuario(Long idUsuario) {
        return (List)this.repository.buscarPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<JugadorJovenDTO> buscarPorNivelHabilidad(String nivel) {
        return (List)this.repository.buscarPorNivelHabilidad(nivel).stream().map(this::toDTO).collect(Collectors.toList());
    }
}
