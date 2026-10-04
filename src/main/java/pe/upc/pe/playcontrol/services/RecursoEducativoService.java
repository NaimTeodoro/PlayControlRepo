package pe.upc.pe.playcontrol.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.RecursoEducativoRequest;
import pe.upc.pe.playcontrol.dtos.RecursoEducativoResponse;
import pe.upc.pe.playcontrol.entities.RecursoEducativo;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.RecursoEducativoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RecursoEducativoService {

    private final RecursoEducativoRepository repository;

    public RecursoEducativoService(RecursoEducativoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RecursoEducativoResponse> listar(String categoria) {
        List<RecursoEducativo> lista = (categoria == null || categoria.isBlank())
                ? repository.findAll()
                : repository.findByCategoriaIgnoreCase(categoria);
        return lista.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RecursoEducativoResponse obtener(Integer id) {
        return toResponse(buscar(id));
    }

    public RecursoEducativoResponse crear(RecursoEducativoRequest request) {
        RecursoEducativo entidad = new RecursoEducativo();
        LocalDateTime ahora = LocalDateTime.now();
        entidad.setFechaPublicacion(ahora);
        entidad.setFechaActualizacion(ahora);
        entidad.setVisitas(0);
        entidad.setActivo(true);
        aplicar(entidad, request);
        return toResponse(repository.save(entidad));
    }

    public RecursoEducativoResponse actualizar(Integer id, RecursoEducativoRequest request) {
        RecursoEducativo entidad = buscar(id);
        aplicar(entidad, request);
        entidad.setFechaActualizacion(LocalDateTime.now());
        return toResponse(repository.save(entidad));
    }

    public void eliminar(Integer id) {
        repository.delete(buscar(id));
    }

    private RecursoEducativo buscar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RecursoEducativo", id));
    }

    private void aplicar(RecursoEducativo e, RecursoEducativoRequest r) {
        e.setTitulo(r.titulo());
        e.setDescripcion(r.descripcion());
        e.setContenido(r.contenido());
        e.setTipoRecurso(r.tipoRecurso());
        e.setFormato(r.formato());
        e.setDuracionMinutos(r.duracionMinutos());
        e.setAudienciaObjetivo(r.audienciaObjetivo());
        e.setCategoria(r.categoria());
        e.setSubcategoria(r.subcategoria());
        e.setNivelDificultad(r.nivelDificultad());
        e.setUrlRecurso(r.urlRecurso());
        e.setUrlImagen(r.urlImagen());
        e.setAutor(r.autor());
        if (r.activo() != null) {
            e.setActivo(r.activo());
        }
    }

    private RecursoEducativoResponse toResponse(RecursoEducativo e) {
        return new RecursoEducativoResponse(
                e.getIdRecurso(),
                e.getTitulo(),
                e.getDescripcion(),
                e.getContenido(),
                e.getTipoRecurso(),
                e.getFormato(),
                e.getDuracionMinutos(),
                e.getAudienciaObjetivo(),
                e.getCategoria(),
                e.getSubcategoria(),
                e.getNivelDificultad(),
                e.getUrlRecurso(),
                e.getUrlImagen(),
                e.getAutor(),
                e.getFechaPublicacion(),
                e.getFechaActualizacion(),
                e.getActivo(),
                e.getVisitas(),
                e.getValoracion());
    }
}
