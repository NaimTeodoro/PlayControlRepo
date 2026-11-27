package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.PadreTutorDTO;
import pe.upc.pe.playcontrol.entities.PadreTutor;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.PadreTutorRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.PadreTutorService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PadreTutorServiceImpl implements PadreTutorService {
    @Autowired
    private PadreTutorRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    private PadreTutorDTO toDTO(PadreTutor e) {
        PadreTutorDTO dto = modelMapper.map(e, PadreTutorDTO.class);
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        return dto;
    }

    private PadreTutor toEntity(PadreTutorDTO dto) {
        PadreTutor e = modelMapper.map(dto, PadreTutor.class);
        User usuario = userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario()));
        e.setUsuario(usuario);
        return e;
    }

    @Override
    public List<PadreTutorDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public PadreTutorDTO buscarPorId(Long id) {
        return toDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PadreTutor", id)));
    }

    @Override
    public PadreTutorDTO insertar(PadreTutorDTO dto) {
        dto.setIdPadreTutor(null);
        if (repository.findByUsuario_IdUsuario(dto.getIdUsuario()).isPresent()) {
            throw new ConflictException("El usuario ya tiene un perfil de padre/tutor");
        }
        PadreTutor e = toEntity(dto);
        if (e.getFechaRegistro() == null) {
            e.setFechaRegistro(LocalDateTime.now());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public PadreTutorDTO actualizar(Long id, PadreTutorDTO dto) {
        PadreTutor existente = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PadreTutor", id));
        PadreTutor e = toEntity(dto);
        e.setIdPadreTutor(id);
        if (e.getFechaRegistro() == null) {
            e.setFechaRegistro(existente.getFechaRegistro());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("PadreTutor", id);
        }
        repository.deleteById(id);
    }

    @Override
    public PadreTutorDTO buscarPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuario(idUsuario).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe perfil de padre/tutor para el usuario " + idUsuario));
    }
}
