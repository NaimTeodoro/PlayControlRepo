package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.LogroDTO;
import pe.upc.pe.playcontrol.entities.Logro;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.LogroRepository;
import pe.upc.pe.playcontrol.services.LogroService;

import java.util.List;

@Service
public class LogroServiceImpl implements LogroService {

    @Autowired
    private LogroRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    private LogroDTO toDTO(Logro e) {
        return modelMapper.map(e, LogroDTO.class);
    }

    private Logro buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Logro", id));
    }

    @Override
    public List<LogroDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public LogroDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public LogroDTO insertar(LogroDTO dto) {
        dto.setIdLogro(null);
        Logro e = modelMapper.map(dto, Logro.class);
        if (e.getActivo() == null) {
            e.setActivo(true);
        }
        return toDTO(repository.save(e));
    }

    @Override
    public LogroDTO actualizar(Long id, LogroDTO dto) {
        Logro existente = buscar(id);
        Logro e = modelMapper.map(dto, Logro.class);
        e.setIdLogro(id);
        if (e.getActivo() == null) {
            e.setActivo(existente.getActivo());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Logro", id);
        }
        repository.deleteById(id);
    }
}
