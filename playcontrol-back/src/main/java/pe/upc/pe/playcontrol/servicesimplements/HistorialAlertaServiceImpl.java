package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.HistorialAlertaDTO;
import pe.upc.pe.playcontrol.entities.AlertaJuego;
import pe.upc.pe.playcontrol.entities.HistorialAlerta;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.AlertaJuegoRepository;
import pe.upc.pe.playcontrol.repositories.HistorialAlertaRepository;
import pe.upc.pe.playcontrol.services.HistorialAlertaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialAlertaServiceImpl implements HistorialAlertaService {

    @Autowired
    private HistorialAlertaRepository repository;
    @Autowired
    private AlertaJuegoRepository alertaJuegoRepository;
    @Autowired
    private ModelMapper modelMapper;

    private HistorialAlertaDTO toDTO(HistorialAlerta e) {
        HistorialAlertaDTO dto = modelMapper.map(e, HistorialAlertaDTO.class);
        AlertaJuego a = e.getAlerta();
        dto.setIdAlerta(a.getIdAlerta());
        dto.setIdUsuario(a.getUsuario().getIdUsuario());
        dto.setTipoAlerta(a.getTipoAlerta());
        dto.setRespuestaUsuario(a.getRespuestaUsuario());
        return dto;
    }

    private HistorialAlerta toEntity(HistorialAlertaDTO dto) {
        HistorialAlerta e = modelMapper.map(dto, HistorialAlerta.class);
        e.setAlerta(alertaJuegoRepository.findById(dto.getIdAlerta())
                .orElseThrow(() -> new ResourceNotFoundException("AlertaJuego", dto.getIdAlerta())));
        return e;
    }

    private HistorialAlerta buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("HistorialAlerta", id));
    }

    @Override
    public List<HistorialAlertaDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public HistorialAlertaDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public HistorialAlertaDTO insertar(HistorialAlertaDTO dto) {
        dto.setIdHistorial(null);
        HistorialAlerta e = toEntity(dto);
        if (e.getFecha() == null) {
            e.setFecha(LocalDateTime.now());
        }
        if (e.getEstado() == null) {
            e.setEstado("ABIERTO");
        }
        return toDTO(repository.save(e));
    }

    @Override
    public HistorialAlertaDTO actualizar(Long id, HistorialAlertaDTO dto) {
        HistorialAlerta existente = buscar(id);
        HistorialAlerta e = toEntity(dto);
        e.setIdHistorial(id);
        if (e.getFecha() == null) {
            e.setFecha(existente.getFecha());
        }
        if (e.getEstado() == null) {
            e.setEstado(existente.getEstado());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("HistorialAlerta", id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<HistorialAlertaDTO> buscarPorAlerta(Long idAlerta) {
        return repository.findByAlerta_IdAlertaOrderByFechaDesc(idAlerta).stream().map(this::toDTO).toList();
    }

    @Override
    public List<HistorialAlertaDTO> buscarPorUsuario(Long idUsuario) {
        return repository.buscarPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }
}
