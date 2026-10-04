package pe.upc.pe.playcontrol.servicesimplements;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.LimiteJuegoDTO;
import pe.upc.pe.playcontrol.entities.LimiteJuego;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.LimiteJuegoRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.LimiteJuegoService;

@Service
public class LimiteJuegoServiceImpl implements LimiteJuegoService {
    @Autowired
    private LimiteJuegoRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public LimiteJuegoServiceImpl() {
    }

    private LimiteJuegoDTO toDTO(LimiteJuego e) {
        LimiteJuegoDTO dto = (LimiteJuegoDTO)this.modelMapper.map(e, LimiteJuegoDTO.class);
        dto.setIdUsuario(e.getUser().getIdUsuario());
        return dto;
    }

    private LimiteJuego toEntity(LimiteJuegoDTO dto) {
        LimiteJuego e = (LimiteJuego)this.modelMapper.map(dto, LimiteJuego.class);
        User user = (User)this.userRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        e.setUser(user);
        return e;
    }

    public List<LimiteJuegoDTO> listar() {
        return (List)this.repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public LimiteJuegoDTO buscarPorId(Long id) {
        return this.toDTO((LimiteJuego)this.repository.findById(id).orElseThrow(() -> new RuntimeException("Limite no encontrado")));
    }

    public LimiteJuegoDTO insertar(LimiteJuegoDTO dto) {
        dto.setIdLimite((Long)null);
        LimiteJuego e = this.toEntity(dto);
        e.setFechaCreacion(LocalDateTime.now());
        e.setUltimaModificacion(LocalDateTime.now());
        if (e.getEstado() == null) {
            e.setEstado("ACTIVO");
        }

        return this.toDTO((LimiteJuego)this.repository.save(e));
    }

    public LimiteJuegoDTO actualizar(Long id, LimiteJuegoDTO dto) {
        LimiteJuego existente = (LimiteJuego)this.repository.findById(id).orElseThrow(() -> new RuntimeException("Limite no encontrado"));
        LimiteJuego e = this.toEntity(dto);
        e.setIdLimite(id);
        e.setFechaCreacion(existente.getFechaCreacion());
        e.setUltimaModificacion(LocalDateTime.now());
        return this.toDTO((LimiteJuego)this.repository.save(e));
    }

    public void eliminar(Long id) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("Límite no encontrado");
        } else {
            this.repository.deleteById(id);
        }
    }

    public List<LimiteJuegoDTO> buscarPorUsuario(Long idUsuario) {
        return (List)this.repository.buscarPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<LimiteJuegoDTO> buscarActivosPorUsuario(Long idUsuario) {
        return (List)this.repository.buscarActivosPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
    }
}
