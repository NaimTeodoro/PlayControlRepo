package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.LimiteJuegoDTO;
import pe.upc.pe.playcontrol.entities.LimiteJuego;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.AccesoDenegadoException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.LimiteJuegoRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.ControlParentalService;
import pe.upc.pe.playcontrol.services.LimiteJuegoService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LimiteJuegoServiceImpl implements LimiteJuegoService {

    private static final String ACTIVO = "ACTIVO";
    private static final String INACTIVO = "INACTIVO";

    // Mensajes de los 403 (el front los muestra tal cual)
    static final String MSG_SOLO_PROPIOS = "Solo puedes gestionar tus propios límites";
    static final String MSG_CONTROL_PADRE = "Tu límite está controlado por tu padre/tutor. Pídele que lo cambie.";
    static final String MSG_PADRE_SIN_PERMISO = "No tienes permiso para configurar los límites de este jugador";
    static final String MSG_PADRE_SIN_VINCULO = "Solo puedes ver los límites de tus hijos con vínculo activo";
    static final String MSG_OTRO_PADRE = "No puedes modificar un límite configurado por otro padre/tutor";
    static final String MSG_SIN_ROL = "No tienes permiso para gestionar límites";

    @Autowired
    private LimiteJuegoRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ControlParentalService control;

    private LimiteJuegoDTO toDTO(LimiteJuego e) {
        LimiteJuegoDTO dto = modelMapper.map(e, LimiteJuegoDTO.class);
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        dto.setIdCreadoPor(e.getCreadoPor() != null ? e.getCreadoPor().getIdUsuario() : null);
        return dto;
    }

    private LimiteJuego toEntity(LimiteJuegoDTO dto) {
        LimiteJuego e = modelMapper.map(dto, LimiteJuego.class);
        e.setUsuario(userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario())));
        if (dto.getIdCreadoPor() != null) {
            e.setCreadoPor(userRepository.findById(dto.getIdCreadoPor())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdCreadoPor())));
        } else {
            e.setCreadoPor(null);
        }
        return e;
    }

    /** Un solo limite ACTIVO por usuario: desactiva los demas. */
    private void desactivarOtros(LimiteJuego activo) {
        if (!ACTIVO.equals(activo.getEstado())) {
            return;
        }
        LocalDateTime ahora = LocalDateTime.now();
        for (LimiteJuego otro : repository.buscarActivosPorUsuario(activo.getUsuario().getIdUsuario())) {
            if (!otro.getIdLimite().equals(activo.getIdLimite())) {
                otro.setEstado(INACTIVO);
                otro.setUltimaModificacion(ahora);
                repository.save(otro);
            }
        }
    }

    // ------------------------------------------------------------------ reglas de propiedad y control parental

    /** Lectura: ADMIN todo; JUGADOR solo los suyos; PADRE solo los de sus hijos con vinculo ACTIVO. */
    private void validarLectura(Long idUsuarioLimite) {
        if (control.esAdmin()) {
            return;
        }
        Long idActual = control.usuarioActual().getIdUsuario();
        if (control.esJugador() && idActual.equals(idUsuarioLimite)) {
            return;
        }
        if (control.esPadre()) {
            if (!control.hijosActivos(idActual).contains(idUsuarioLimite)) {
                throw new AccesoDenegadoException(MSG_PADRE_SIN_VINCULO);
            }
            return;
        }
        if (control.esJugador()) {
            throw new AccesoDenegadoException(MSG_SOLO_PROPIOS);
        }
        throw new AccesoDenegadoException(MSG_SIN_ROL);
    }

    /**
     * Escritura (POST / PUT / DELETE).
     * idUsuarioDestino: jugador al que quedara asignado el limite. existente: limite actual (null en POST).
     * Devuelve el usuario que actua (sera el idCreadoPor), o null si es ADMIN (sin restricciones).
     */
    private User validarEscritura(Long idUsuarioDestino, LimiteJuego existente) {
        if (control.esAdmin()) {
            return null;
        }
        User actual = control.usuarioActual();
        Long idActual = actual.getIdUsuario();
        boolean propio = idActual.equals(idUsuarioDestino)
                && (existente == null || idActual.equals(existente.getUsuario().getIdUsuario()));

        // JUGADOR: solo sus limites y solo si su padre no tiene el control
        if (control.esJugador() && (propio || !control.esPadre())) {
            if (!propio) {
                throw new AccesoDenegadoException(MSG_SOLO_PROPIOS);
            }
            if (control.limiteControladoPorPadre(idActual)) {
                throw new AccesoDenegadoException(MSG_CONTROL_PADRE);
            }
            return actual;
        }

        // PADRE: hijos con vinculo ACTIVO y permiso CONFIGURAR_LIMITES; no pisa limites de otro padre
        if (control.esPadre()) {
            if (!control.padrePuedeConfigurar(idActual, idUsuarioDestino)) {
                throw new AccesoDenegadoException(MSG_PADRE_SIN_PERMISO);
            }
            if (existente != null) {
                Long idDuenoActual = existente.getUsuario().getIdUsuario();
                if (!idDuenoActual.equals(idUsuarioDestino) && !control.padrePuedeConfigurar(idActual, idDuenoActual)) {
                    throw new AccesoDenegadoException(MSG_PADRE_SIN_PERMISO);
                }
                User creador = existente.getCreadoPor();
                if (creador != null && !creador.getIdUsuario().equals(idActual)
                        && control.tieneRol(creador, ControlParentalService.ROLE_PADRE)) {
                    throw new AccesoDenegadoException(MSG_OTRO_PADRE);
                }
            }
            return actual;
        }
        throw new AccesoDenegadoException(MSG_SIN_ROL);
    }

    private LimiteJuego buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("LimiteJuego", id));
    }

    // ------------------------------------------------------------------ CRUD

    @Override
    public List<LimiteJuegoDTO> listar() {
        if (control.esAdmin()) {
            return repository.findAll().stream().map(this::toDTO).toList();
        }
        // Jugador: los suyos. Padre: los de sus hijos con vinculo ACTIVO.
        Long idActual = control.usuarioActual().getIdUsuario();
        Set<Long> visibles = new HashSet<>();
        if (control.esJugador()) {
            visibles.add(idActual);
        }
        if (control.esPadre()) {
            visibles.addAll(control.hijosActivos(idActual));
        }
        return repository.findAll().stream()
                .filter(l -> visibles.contains(l.getUsuario().getIdUsuario()))
                .map(this::toDTO).toList();
    }

    @Override
    public LimiteJuegoDTO buscarPorId(Long id) {
        LimiteJuego e = buscar(id);
        validarLectura(e.getUsuario().getIdUsuario());
        return toDTO(e);
    }

    @Override
    @Transactional
    public LimiteJuegoDTO insertar(LimiteJuegoDTO dto) {
        User actor = validarEscritura(dto.getIdUsuario(), null);
        if (actor != null) {
            dto.setIdCreadoPor(actor.getIdUsuario()); // no-ADMIN: se ignora el idCreadoPor del body
        }
        dto.setIdLimite(null);
        LimiteJuego e = toEntity(dto);
        LocalDateTime ahora = LocalDateTime.now();
        e.setFechaCreacion(ahora);
        e.setUltimaModificacion(ahora);
        if (e.getEstado() == null) {
            e.setEstado(ACTIVO);
        }
        e = repository.save(e);
        desactivarOtros(e);
        return toDTO(e);
    }

    @Override
    @Transactional
    public LimiteJuegoDTO actualizar(Long id, LimiteJuegoDTO dto) {
        LimiteJuego existente = buscar(id);
        User actor = validarEscritura(dto.getIdUsuario(), existente);
        if (actor != null) {
            dto.setIdCreadoPor(actor.getIdUsuario()); // no-ADMIN: se ignora el idCreadoPor del body
        }
        LimiteJuego e = toEntity(dto);
        e.setIdLimite(id);
        e.setFechaCreacion(existente.getFechaCreacion());
        e.setUltimaModificacion(LocalDateTime.now());
        if (e.getEstado() == null) {
            e.setEstado(existente.getEstado());
        }
        if (e.getCreadoPor() == null) {
            e.setCreadoPor(existente.getCreadoPor());
        }
        e = repository.save(e);
        desactivarOtros(e);
        return toDTO(e);
    }

    @Override
    public void eliminar(Long id) {
        LimiteJuego existente = buscar(id);
        validarEscritura(existente.getUsuario().getIdUsuario(), existente);
        repository.deleteById(id);
    }

    @Override
    public List<LimiteJuegoDTO> buscarPorUsuario(Long idUsuario) {
        validarLectura(idUsuario);
        return repository.buscarPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    public List<LimiteJuegoDTO> buscarActivosPorUsuario(Long idUsuario) {
        validarLectura(idUsuario);
        return repository.buscarActivosPorUsuario(idUsuario).stream().map(this::toDTO).toList();
    }
}
