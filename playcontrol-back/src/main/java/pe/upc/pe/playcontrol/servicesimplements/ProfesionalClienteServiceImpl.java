package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.ProfesionalClienteDTO;
import pe.upc.pe.playcontrol.entities.JugadorJoven;
import pe.upc.pe.playcontrol.entities.ProfesionalCliente;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.JugadorJovenRepository;
import pe.upc.pe.playcontrol.repositories.ProfesionalClienteRepository;
import pe.upc.pe.playcontrol.repositories.ProfesionalSaludRepository;
import pe.upc.pe.playcontrol.services.ProfesionalClienteService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfesionalClienteServiceImpl implements ProfesionalClienteService {

    private static final String ESTADO_POR_DEFECTO = "ACTIVO";

    @Autowired
    private ProfesionalClienteRepository repository;
    @Autowired
    private ProfesionalSaludRepository profesionalRepository;
    @Autowired
    private JugadorJovenRepository jugadorJovenRepository;
    @Autowired
    private ModelMapper modelMapper;

    private ProfesionalClienteDTO toDTO(ProfesionalCliente e) {
        ProfesionalClienteDTO dto = modelMapper.map(e, ProfesionalClienteDTO.class);
        dto.setIdProfesional(e.getProfesional().getIdProfesional());
        JugadorJoven joven = e.getJugadorJoven();
        User jugador = joven.getUsuario();
        dto.setIdJugadorJoven(joven.getIdJugadorJoven());
        dto.setIdUsuarioJugador(jugador.getIdUsuario());
        dto.setNombreJugador(jugador.getNombre());
        dto.setApellidoJugador(jugador.getApellido());
        return dto;
    }

    private ProfesionalCliente toEntity(ProfesionalClienteDTO dto) {
        ProfesionalCliente e = modelMapper.map(dto, ProfesionalCliente.class);
        e.setProfesional(profesionalRepository.findById(dto.getIdProfesional())
                .orElseThrow(() -> new ResourceNotFoundException("ProfesionalSalud", dto.getIdProfesional())));
        e.setJugadorJoven(jugadorJovenRepository.findById(dto.getIdJugadorJoven())
                .orElseThrow(() -> new ResourceNotFoundException("JugadorJoven", dto.getIdJugadorJoven())));
        return e;
    }

    private ProfesionalCliente buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProfesionalCliente", id));
    }

    private boolean existePar(Long idProfesional, Long idJugadorJoven) {
        return !repository.findByProfesional_IdProfesionalAndJugadorJoven_IdJugadorJoven(idProfesional, idJugadorJoven).isEmpty();
    }

    @Override
    public List<ProfesionalClienteDTO> listar(Long idProfesional, Long idJugadorJoven) {
        List<ProfesionalCliente> lista;
        if (idProfesional != null && idJugadorJoven != null) {
            lista = repository.findByProfesional_IdProfesionalAndJugadorJoven_IdJugadorJoven(idProfesional, idJugadorJoven);
        } else if (idProfesional != null) {
            lista = repository.findByProfesional_IdProfesional(idProfesional);
        } else if (idJugadorJoven != null) {
            lista = repository.findByJugadorJoven_IdJugadorJoven(idJugadorJoven);
        } else {
            lista = repository.findAll();
        }
        return lista.stream().map(this::toDTO).toList();
    }

    @Override
    public ProfesionalClienteDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public ProfesionalClienteDTO insertar(ProfesionalClienteDTO dto) {
        dto.setIdProfesionalCliente(null);
        if (existePar(dto.getIdProfesional(), dto.getIdJugadorJoven())) {
            throw new ConflictException("El profesional ya tiene registrado a este jugador como cliente");
        }
        ProfesionalCliente e = toEntity(dto);
        if (e.getFechaAsociacion() == null) {
            e.setFechaAsociacion(LocalDateTime.now());
        }
        if (e.getEstado() == null) {
            e.setEstado(ESTADO_POR_DEFECTO);
        }
        return toDTO(repository.save(e));
    }

    @Override
    public ProfesionalClienteDTO actualizar(Long id, ProfesionalClienteDTO dto) {
        ProfesionalCliente existente = buscar(id);
        boolean cambiaPar = !existente.getProfesional().getIdProfesional().equals(dto.getIdProfesional())
                || !existente.getJugadorJoven().getIdJugadorJoven().equals(dto.getIdJugadorJoven());
        if (cambiaPar && existePar(dto.getIdProfesional(), dto.getIdJugadorJoven())) {
            throw new ConflictException("El profesional ya tiene registrado a este jugador como cliente");
        }
        ProfesionalCliente e = toEntity(dto);
        e.setIdProfesionalCliente(id);
        e.setFechaAsociacion(existente.getFechaAsociacion());
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
    public List<ProfesionalClienteDTO> buscarPorProfesional(Long idProfesional) {
        return repository.findByProfesional_IdProfesional(idProfesional).stream().map(this::toDTO).toList();
    }
}
