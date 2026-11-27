package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.PlataformaDTO;
import pe.upc.pe.playcontrol.entities.Plataforma;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.PlataformaRepository;
import pe.upc.pe.playcontrol.services.PlataformaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlataformaServiceImpl implements PlataformaService {

    @Autowired
    private PlataformaRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    private PlataformaDTO toDTO(Plataforma e) {
        return modelMapper.map(e, PlataformaDTO.class);
    }

    private Plataforma buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plataforma", id));
    }

    @Override
    public List<PlataformaDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public PlataformaDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public PlataformaDTO insertar(PlataformaDTO dto) {
        dto.setIdPlataforma(null);
        Plataforma e = modelMapper.map(dto, Plataforma.class);
        if (e.getActiva() == null) {
            e.setActiva(true);
        }
        if (e.getFechaCreacion() == null) {
            e.setFechaCreacion(LocalDateTime.now());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public PlataformaDTO actualizar(Long id, PlataformaDTO dto) {
        Plataforma existente = buscar(id);
        Plataforma e = modelMapper.map(dto, Plataforma.class);
        e.setIdPlataforma(id);
        if (e.getActiva() == null) {
            e.setActiva(existente.getActiva());
        }
        if (e.getFechaCreacion() == null) {
            e.setFechaCreacion(existente.getFechaCreacion());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Plataforma", id);
        }
        repository.deleteById(id);
    }
}
