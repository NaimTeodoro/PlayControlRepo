package pe.upc.pe.playcontrol.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.CitaDTO;
import pe.upc.pe.playcontrol.entities.JugadorJoven;
import pe.upc.pe.playcontrol.entities.ProfesionalCliente;
import pe.upc.pe.playcontrol.entities.ProfesionalSalud;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.AccesoDenegadoException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.JugadorJovenRepository;
import pe.upc.pe.playcontrol.repositories.ProfesionalClienteRepository;
import pe.upc.pe.playcontrol.services.CitaService;
import pe.upc.pe.playcontrol.services.ControlParentalService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private ProfesionalClienteRepository repository;
    @Autowired
    private JugadorJovenRepository jugadorJovenRepository;
    @Autowired
    private ControlParentalService control;

    /**
     * Armado campo por campo (sin ModelMapper): solo datos de agenda y de contacto del profesional.
     * diagnostico, planTratamiento, notas, numeroLicencia, etc. quedan fuera a proposito.
     */
    private CitaDTO toDTO(ProfesionalCliente e) {
        CitaDTO dto = new CitaDTO();
        dto.setIdProfesionalCliente(e.getIdProfesionalCliente());
        dto.setEstado(e.getEstado());
        dto.setFrecuenciaConsulta(e.getFrecuenciaConsulta());
        dto.setProximaConsulta(e.getProximaConsulta());

        JugadorJoven joven = e.getJugadorJoven();
        User jugador = joven.getUsuario();
        dto.setIdJugadorJoven(joven.getIdJugadorJoven());
        dto.setIdUsuarioJugador(jugador.getIdUsuario());
        dto.setNombreJugador(jugador.getNombre());
        dto.setApellidoJugador(jugador.getApellido());

        ProfesionalSalud profesional = e.getProfesional();
        User usuarioProfesional = profesional.getUsuario();
        dto.setIdProfesional(profesional.getIdProfesional());
        dto.setNombreProfesional(usuarioProfesional.getNombre());
        dto.setApellidoProfesional(usuarioProfesional.getApellido());
        dto.setEspecialidad(profesional.getEspecialidad());
        dto.setInstitucion(profesional.getInstitucion());
        dto.setTelefonoConsulta(profesional.getTelefonoConsulta());
        dto.setHorarioConsulta(profesional.getHorarioConsulta());
        return dto;
    }

    /** Citas ACTIVO de esos jugadores (ids de perfil). Coleccion vacia -> lista vacia sin consultar. */
    private List<CitaDTO> citasDe(Collection<Long> idsJugadorJoven) {
        if (idsJugadorJoven == null || idsJugadorJoven.isEmpty()) {
            return List.of();
        }
        return repository.buscarCitasActivasPorJovenes(idsJugadorJoven).stream().map(this::toDTO).toList();
    }

    @Override
    public List<CitaDTO> misCitas() {
        if (control.esAdmin()) {
            return repository.buscarCitasActivas().stream().map(this::toDTO).toList();
        }
        Long idUsuario = control.usuarioActual().getIdUsuario();
        if (control.esPadre()) {
            return citasDe(control.jovenesActivos(idUsuario));
        }
        if (control.esJugador()) {
            Long idJugadorJoven = control.idJugadorJovenDe(idUsuario);
            return idJugadorJoven == null ? List.of() : citasDe(List.of(idJugadorJoven));
        }
        throw new AccesoDenegadoException("No tienes permiso para ver citas");
    }

    @Override
    public List<CitaDTO> citasDeJugador(Long idJugadorJoven) {
        if (!jugadorJovenRepository.existsById(idJugadorJoven)) {
            throw new ResourceNotFoundException("JugadorJoven", idJugadorJoven);
        }
        if (!control.esAdmin()) {
            Long idUsuario = control.usuarioActual().getIdUsuario();
            if (control.esPadre()) {
                Set<Long> jovenes = control.jovenesActivos(idUsuario);
                if (!jovenes.contains(idJugadorJoven)) {
                    throw new AccesoDenegadoException("No tienes un vínculo activo con este jugador");
                }
            } else if (control.esJugador()) {
                if (!idJugadorJoven.equals(control.idJugadorJovenDe(idUsuario))) {
                    throw new AccesoDenegadoException("Solo puedes ver tus propias citas");
                }
            } else {
                throw new AccesoDenegadoException("No tienes permiso para ver citas");
            }
        }
        return citasDe(List.of(idJugadorJoven));
    }
}
