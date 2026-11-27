package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.ProfesionalSaludDTO;
import pe.upc.pe.playcontrol.entities.ProfesionalSalud;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.ProfesionalSaludRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.ProfesionalSaludService;

import java.util.List;

@Service
public class ProfesionalSaludServiceImpl implements ProfesionalSaludService {

    private static final String ESTADO_POR_DEFECTO = "ACTIVO";

    @Autowired
    private ProfesionalSaludRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    private ProfesionalSaludDTO toDTO(ProfesionalSalud e) {
        ProfesionalSaludDTO dto = modelMapper.map(e, ProfesionalSaludDTO.class);
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        return dto;
    }

    private ProfesionalSalud toEntity(ProfesionalSaludDTO dto) {
        ProfesionalSalud e = modelMapper.map(dto, ProfesionalSalud.class);
        User usuario = userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario()));
        e.setUsuario(usuario);
        return e;
    }

    private ProfesionalSalud buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProfesionalSalud", id));
    }

    @Override
    public List<ProfesionalSaludDTO> listar(String estado) {
        List<ProfesionalSalud> lista = (estado == null || estado.isBlank())
                ? repository.findAll()
                : repository.findByEstado(estado.trim().toUpperCase());
        return lista.stream().map(this::toDTO).toList();
    }

    @Override
    public ProfesionalSaludDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public ProfesionalSaludDTO insertar(ProfesionalSaludDTO dto) {
        dto.setIdProfesional(null);
        if (repository.findByUsuario_IdUsuario(dto.getIdUsuario()).isPresent()) {
            throw new ConflictException("El usuario ya tiene un perfil de profesional");
        }
        ProfesionalSalud e = toEntity(dto);
        if (e.getEstado() == null) {
            e.setEstado(ESTADO_POR_DEFECTO);
        }
        return toDTO(repository.save(e));
    }

    @Override
    public ProfesionalSaludDTO actualizar(Long id, ProfesionalSaludDTO dto) {
        ProfesionalSalud existente = buscar(id);
        ProfesionalSalud e = toEntity(dto);
        e.setIdProfesional(id);
        if (e.getEstado() == null) {
            e.setEstado(existente.getEstado());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    @Override
    public ProfesionalSaludDTO buscarPorUsuario(Long idUsuario) {
        return repository.findByUsuario_IdUsuario(idUsuario).map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No existe perfil de profesional para el usuario " + idUsuario));
    }
}
