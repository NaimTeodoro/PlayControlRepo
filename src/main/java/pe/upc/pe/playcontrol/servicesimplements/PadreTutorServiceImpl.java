package pe.upc.pe.playcontrol.servicesimplements;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.PadreTutorDTO;
import pe.upc.pe.playcontrol.entities.PadreTutor;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.PadreTutorRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.PadreTutorService;

@Service
public class PadreTutorServiceImpl implements PadreTutorService {
    @Autowired
    private PadreTutorRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public PadreTutorServiceImpl() {
    }

    private PadreTutorDTO toDTO(PadreTutor e) {
        PadreTutorDTO dto = (PadreTutorDTO)this.modelMapper.map(e, PadreTutorDTO.class);
        dto.setIdUsuario(e.getUser().getIdUsuario());
        return dto;
    }

    private PadreTutor toEntity(PadreTutorDTO dto) {
        PadreTutor e = (PadreTutor)this.modelMapper.map(dto, PadreTutor.class);
        User user = (User)this.userRepository.findById(dto.getIdUsuario()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        e.setUser(user);
        return e;
    }

    public List<PadreTutorDTO> listar() {
        return (List)this.repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PadreTutorDTO buscarPorId(Long id) {
        return this.toDTO((PadreTutor)this.repository.findById(id).orElseThrow(() -> new RuntimeException("PadreTutor no encontrado")));
    }

    public PadreTutorDTO insertar(PadreTutorDTO dto) {
        dto.setIdPadreTutor((Long)null);
        PadreTutor e = this.toEntity(dto);
        if (e.getFechaRegistro() == null) {
            e.setFechaRegistro(LocalDateTime.now());
        }

        return this.toDTO((PadreTutor)this.repository.save(e));
    }

    public PadreTutorDTO actualizar(Long id, PadreTutorDTO dto) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("PadreTutor no encontrado");
        } else {
            PadreTutor e = this.toEntity(dto);
            e.setIdPadreTutor(id);
            return this.toDTO((PadreTutor)this.repository.save(e));
        }
    }

    public void eliminar(Long id) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("PadreTutor no encontrado");
        } else {
            this.repository.deleteById(id);
        }
    }

    public List<PadreTutorDTO> buscarPorUsuario(Long idUsuario) {
        return (List)this.repository.buscarPorUsuario(idUsuario).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PadreTutorDTO> buscarPorNivelTecnologia(String nivel) {
        return (List)this.repository.buscarPorNivelTecnologia(nivel).stream().map(this::toDTO).collect(Collectors.toList());
    }
}
