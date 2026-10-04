package pe.upc.pe.playcontrol.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dto.ProfesionalClienteRequest;
import pe.upc.pe.playcontrol.dto.ProfesionalClienteResponse;
import pe.upc.pe.playcontrol.entity.ProfesionalCliente;
import pe.upc.pe.playcontrol.entity.ProfesionalSalud;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repository.ProfesionalClienteRepository;
import pe.upc.pe.playcontrol.repository.ProfesionalSaludRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProfesionalClienteService {

    private static final String ESTADO_POR_DEFECTO = "ACTIVO";

    private final ProfesionalClienteRepository repository;
    private final ProfesionalSaludRepository profesionalRepository;

    public ProfesionalClienteService(ProfesionalClienteRepository repository,
                                     ProfesionalSaludRepository profesionalRepository) {
        this.repository = repository;
        this.profesionalRepository = profesionalRepository;
    }

    @Transactional(readOnly = true)
    public List<ProfesionalClienteResponse> listar(Integer idProfesional, Integer idJugadorJoven) {
        List<ProfesionalCliente> lista;
        if (idProfesional != null && idJugadorJoven != null) {
            lista = repository.findByProfesional_IdProfesionalAndIdJugadorJoven(idProfesional, idJugadorJoven);
        } else if (idProfesional != null) {
            lista = repository.findByProfesional_IdProfesional(idProfesional);
        } else if (idJugadorJoven != null) {
            lista = repository.findByIdJugadorJoven(idJugadorJoven);
        } else {
            lista = repository.findAll();
        }
        return lista.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProfesionalClienteResponse obtener(Integer id) {
        return toResponse(buscar(id));
    }

    public ProfesionalClienteResponse crear(ProfesionalClienteRequest request) {
        // TODO: validar que idJugadorJoven exista cuando esté disponible JugadorJovenRepository
        ProfesionalCliente entidad = new ProfesionalCliente();
        entidad.setProfesional(buscarProfesional(request.idProfesional()));
        entidad.setFechaAsociacion(LocalDateTime.now());
        entidad.setEstado(ESTADO_POR_DEFECTO);
        aplicar(entidad, request);
        return toResponse(repository.save(entidad));
    }

    public ProfesionalClienteResponse actualizar(Integer id, ProfesionalClienteRequest request) {
        ProfesionalCliente entidad = buscar(id);
        if (!entidad.getProfesional().getIdProfesional().equals(request.idProfesional())) {
            entidad.setProfesional(buscarProfesional(request.idProfesional()));
        }
        aplicar(entidad, request);
        return toResponse(repository.save(entidad));
    }

    public void eliminar(Integer id) {
        repository.delete(buscar(id));
    }

    private ProfesionalCliente buscar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProfesionalCliente", id));
    }

    private ProfesionalSalud buscarProfesional(Integer idProfesional) {
        return profesionalRepository.findById(idProfesional)
                .orElseThrow(() -> new ResourceNotFoundException("ProfesionalSalud", idProfesional));
    }

    private void aplicar(ProfesionalCliente e, ProfesionalClienteRequest r) {
        e.setIdJugadorJoven(r.idJugadorJoven());
        e.setDiagnostico(r.diagnostico());
        e.setPlanTratamiento(r.planTratamiento());
        e.setFrecuenciaConsulta(r.frecuenciaConsulta());
        e.setProximaConsulta(r.proximaConsulta());
        e.setNotas(r.notas());
        if (r.estado() != null) {
            e.setEstado(r.estado());
        }
    }

    private ProfesionalClienteResponse toResponse(ProfesionalCliente e) {
        return new ProfesionalClienteResponse(
                e.getIdProfesionalCliente(),
                e.getProfesional().getIdProfesional(),
                e.getIdJugadorJoven(),
                e.getFechaAsociacion(),
                e.getEstado(),
                e.getDiagnostico(),
                e.getPlanTratamiento(),
                e.getFrecuenciaConsulta(),
                e.getProximaConsulta(),
                e.getNotas());
    }
}
