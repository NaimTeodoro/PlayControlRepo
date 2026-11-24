package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.RecursoEducativoDTO;
import pe.upc.pe.playcontrol.entities.RecursoEducativo;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.RecursoEducativoRepository;
import pe.upc.pe.playcontrol.services.RecursoEducativoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecursoEducativoServiceImpl implements RecursoEducativoService {

    @Autowired
    private RecursoEducativoRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    private RecursoEducativoDTO toDTO(RecursoEducativo e) {
        return modelMapper.map(e, RecursoEducativoDTO.class);
    }

    private RecursoEducativo buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("RecursoEducativo", id));
    }

    private static boolean vacio(String s) {
        return s == null || s.isBlank();
    }

    @Override
    public List<RecursoEducativoDTO> listar(String categoria, String audiencia) {
        List<RecursoEducativo> lista;
        if (!vacio(categoria)) {
            lista = repository.findByCategoriaIgnoreCase(categoria.trim());
            if (!vacio(audiencia)) {
                lista = lista.stream().filter(r -> audiencia.trim().equalsIgnoreCase(r.getAudienciaObjetivo())).toList();
            }
        } else if (!vacio(audiencia)) {
            lista = repository.findByAudienciaObjetivoIgnoreCase(audiencia.trim());
        } else {
            lista = repository.findAll();
        }
        return lista.stream().map(this::toDTO).toList();
    }

    @Override
    public RecursoEducativoDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public RecursoEducativoDTO insertar(RecursoEducativoDTO dto) {
        dto.setIdRecurso(null);
        RecursoEducativo e = modelMapper.map(dto, RecursoEducativo.class);
        LocalDateTime ahora = LocalDateTime.now();
        e.setFechaPublicacion(ahora);
        e.setFechaActualizacion(ahora);
        // visitas y valoracion los calcula el servidor (ver usuario-recursos)
        e.setVisitas(0);
        e.setValoracion(BigDecimal.ZERO);
        if (e.getActivo() == null) {
            e.setActivo(true);
        }
        return toDTO(repository.save(e));
    }

    @Override
    public RecursoEducativoDTO actualizar(Long id, RecursoEducativoDTO dto) {
        RecursoEducativo existente = buscar(id);
        RecursoEducativo e = modelMapper.map(dto, RecursoEducativo.class);
        e.setIdRecurso(id);
        e.setFechaPublicacion(existente.getFechaPublicacion());
        e.setFechaActualizacion(LocalDateTime.now());
        e.setVisitas(existente.getVisitas());
        e.setValoracion(existente.getValoracion());
        if (e.getActivo() == null) {
            e.setActivo(existente.getActivo());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }
}
