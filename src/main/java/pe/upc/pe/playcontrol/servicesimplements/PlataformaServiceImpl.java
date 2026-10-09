package pe.upc.pe.playcontrol.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.PlataformaDTO;
import pe.upc.pe.playcontrol.entities.Plataforma;
import pe.upc.pe.playcontrol.repositories.PlataformaRepository;
import pe.upc.pe.playcontrol.services.PlataformaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlataformaServiceImpl implements PlataformaService {

    @Autowired
    private PlataformaRepository repository;

    @Override
    public PlataformaDTO registrar(PlataformaDTO request) {
        Plataforma entity = new Plataforma();
        mapToEntity(request, entity);
        if(entity.getFechaCreacion() == null) {
            entity.setFechaCreacion(LocalDateTime.now());
        }
        Plataforma saved = repository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public List<PlataformaDTO> listar() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public PlataformaDTO obtenerPorId(Long id) {
        Plataforma entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plataforma no encontrada con ID: " + id));
        return mapToDto(entity);
    }

    @Override
    public PlataformaDTO actualizar(PlataformaDTO request) {
        Plataforma entity = repository.findById(request.getIdPlataforma())
                .orElseThrow(() -> new RuntimeException("Plataforma no encontrada con ID: " + request.getIdPlataforma()));
        mapToEntity(request, entity);
        Plataforma saved = repository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Plataforma no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }

    private void mapToEntity(PlataformaDTO dto, Plataforma entity) {
        entity.setNombrePlataforma(dto.getNombrePlataforma());
        entity.setTipo(dto.getTipo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setDesarrollador(dto.getDesarrollador());
        entity.setSistemaOperativo(dto.getSistemaOperativo());
        entity.setActiva(dto.getActiva() != null ? dto.getActiva() : true);
        if (dto.getFechaCreacion() != null) {
            entity.setFechaCreacion(dto.getFechaCreacion());
        }
    }

    private PlataformaDTO mapToDto(Plataforma entity) {
        PlataformaDTO dto = new PlataformaDTO();
        dto.setIdPlataforma(entity.getIdPlataforma());
        dto.setNombrePlataforma(entity.getNombrePlataforma());
        dto.setTipo(entity.getTipo());
        dto.setDescripcion(entity.getDescripcion());
        dto.setDesarrollador(entity.getDesarrollador());
        dto.setSistemaOperativo(entity.getSistemaOperativo());
        dto.setActiva(entity.getActiva());
        dto.setFechaCreacion(entity.getFechaCreacion());
        return dto;
    }
}