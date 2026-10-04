package pe.upc.pe.playcontrol.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.ProfesionalSaludRequest;
import pe.upc.pe.playcontrol.dtos.ProfesionalSaludResponse;

import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.ProfesionalSaludRepository;
import pe.upc.pe.playcontrol.entities.ProfesionalSalud;
import java.util.List;

@Service
@Transactional
public class ProfesionalSaludService {

    private static final String ESTADO_POR_DEFECTO = "ACTIVO";

    private final ProfesionalSaludRepository repository;

    public ProfesionalSaludService(ProfesionalSaludRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ProfesionalSaludResponse> listar(String estado) {
        List<ProfesionalSalud> lista = (estado == null || estado.isBlank())
                ? repository.findAll()
                : repository.findByEstado(estado);
        return lista.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProfesionalSaludResponse obtener(Integer id) {
        return toResponse(buscar(id));
    }

    public ProfesionalSaludResponse crear(ProfesionalSaludRequest request) {
        // TODO: validar que idUsuario exista cuando esté disponible UsuarioRepository
        ProfesionalSalud entidad = new ProfesionalSalud();
        entidad.setEstado(ESTADO_POR_DEFECTO);
        aplicar(entidad, request);
        return toResponse(repository.save(entidad));
    }

    public ProfesionalSaludResponse actualizar(Integer id, ProfesionalSaludRequest request) {
        ProfesionalSalud entidad = buscar(id);
        aplicar(entidad, request);
        return toResponse(repository.save(entidad));
    }

    public void eliminar(Integer id) {
        repository.delete(buscar(id));
    }

    private ProfesionalSalud buscar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProfesionalSalud", id));
    }

    private void aplicar(ProfesionalSalud e, ProfesionalSaludRequest r) {
        e.setIdUsuario(r.idUsuario());
        e.setEspecialidad(r.especialidad());
        e.setNumeroLicencia(r.numeroLicencia());
        e.setInstitucion(r.institucion());
        e.setAniosExperiencia(r.aniosExperiencia());
        e.setAreaTrabajo(r.areaTrabajo());
        e.setCertificaciones(r.certificaciones());
        e.setTelefonoConsulta(r.telefonoConsulta());
        e.setHorarioConsulta(r.horarioConsulta());
        e.setVerificadoPor(r.verificadoPor());
        e.setFechaVerificacion(r.fechaVerificacion());
        if (r.estado() != null) {
            e.setEstado(r.estado());
        }
    }

    private ProfesionalSaludResponse toResponse(ProfesionalSalud e) {
        return new ProfesionalSaludResponse(
                e.getIdProfesional(),
                e.getIdUsuario(),
                e.getEspecialidad(),
                e.getNumeroLicencia(),
                e.getInstitucion(),
                e.getAniosExperiencia(),
                e.getAreaTrabajo(),
                e.getCertificaciones(),
                e.getTelefonoConsulta(),
                e.getHorarioConsulta(),
                e.getVerificadoPor(),
                e.getFechaVerificacion(),
                e.getEstado());
    }
}
