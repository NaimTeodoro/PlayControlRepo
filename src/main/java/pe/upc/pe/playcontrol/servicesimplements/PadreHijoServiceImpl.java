package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.PadreHijoDTO;
import pe.upc.pe.playcontrol.dtos.VincularDTO;
import pe.upc.pe.playcontrol.entities.JugadorJoven;
import pe.upc.pe.playcontrol.entities.PadreHijo;
import pe.upc.pe.playcontrol.entities.PadreTutor;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.AccesoDenegadoException;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.JugadorJovenRepository;
import pe.upc.pe.playcontrol.repositories.PadreHijoRepository;
import pe.upc.pe.playcontrol.repositories.PadreTutorRepository;
import pe.upc.pe.playcontrol.services.ControlParentalService;
import pe.upc.pe.playcontrol.services.PadreHijoService;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PadreHijoServiceImpl implements PadreHijoService {

    private static final String PERMISOS_POR_DEFECTO = "VER_REPORTES,CONFIGURAR_LIMITES";
    private static final String PENDIENTE = "PENDIENTE";

    // Mensajes de los 403 (el front los muestra tal cual)
    static final String MSG_JUGADOR_NO_MODIFICA =
            "No puedes crear, modificar ni eliminar vínculos con tu padre/tutor. Solo puedes autorizar solicitudes pendientes.";
    static final String MSG_SOLO_JUGADOR_AUTORIZA = "Solo el jugador puede autorizar el vínculo";
    static final String MSG_SOLO_PENDIENTE = "Solo puedes autorizar solicitudes de vínculo pendientes";
    static final String MSG_VINCULO_AJENO = "Solo puedes gestionar tus propios vínculos";
    static final String MSG_VER_AJENO = "Solo puedes ver tus propios vínculos";
    static final String MSG_NO_CAMBIAR_PAR = "No puedes cambiar el padre/tutor o el jugador de un vínculo existente";
    static final String MSG_SIN_PERFIL_PADRE = "Necesitas un perfil de padre/tutor para gestionar vínculos";

    @Autowired
    private PadreHijoRepository repository;
    @Autowired
    private PadreTutorRepository padreTutorRepository;
    @Autowired
    private JugadorJovenRepository jugadorJovenRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ControlParentalService control;

    private PadreHijoDTO toDTO(PadreHijo e) {
        PadreHijoDTO dto = modelMapper.map(e, PadreHijoDTO.class);
        dto.setIdPadreTutor(e.getPadreTutor().getIdPadreTutor());
        JugadorJoven joven = e.getJugadorJoven();
        User hijo = joven.getUsuario();
        dto.setIdJugadorJoven(joven.getIdJugadorJoven());
        dto.setIdUsuarioHijo(hijo.getIdUsuario());
        dto.setUsernameHijo(hijo.getUsername());
        dto.setNombreHijo(hijo.getNombre());
        dto.setApellidoHijo(hijo.getApellido());
        dto.setCodigoVinculacionHijo(joven.getCodigoVinculacion());
        return dto;
    }

    private PadreHijo toEntity(PadreHijoDTO dto) {
        PadreHijo e = modelMapper.map(dto, PadreHijo.class);
        e.setPadreTutor(buscarPadre(dto.getIdPadreTutor()));
        e.setJugadorJoven(jugadorJovenRepository.findById(dto.getIdJugadorJoven())
                .orElseThrow(() -> new ResourceNotFoundException("JugadorJoven", dto.getIdJugadorJoven())));
        return e;
    }

    private PadreTutor buscarPadre(Long idPadreTutor) {
        return padreTutorRepository.findById(idPadreTutor)
                .orElseThrow(() -> new ResourceNotFoundException("PadreTutor", idPadreTutor));
    }

    private PadreHijo buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PadreHijo", id));
    }

    private void validarNoDuplicado(Long idPadreTutor, Long idJugadorJoven) {
        if (repository.existsByPadreTutor_IdPadreTutorAndJugadorJoven_IdJugadorJoven(idPadreTutor, idJugadorJoven)) {
            throw new ConflictException("El padre/tutor ya está vinculado con este jugador");
        }
    }

    // ------------------------------------------------------------------ reglas de propiedad

    /** Quien hace la peticion: admin, o sus perfiles de padre / jugador (null si no los tiene). */
    private record Actor(boolean admin, Long idPadreTutor, Long idJugadorJoven) {
        boolean esPadreDe(PadreHijo v) {
            return idPadreTutor != null && idPadreTutor.equals(v.getPadreTutor().getIdPadreTutor());
        }

        boolean esHijoDe(PadreHijo v) {
            return idJugadorJoven != null && idJugadorJoven.equals(v.getJugadorJoven().getIdJugadorJoven());
        }
    }

    private Actor actor() {
        if (control.esAdmin()) {
            return new Actor(true, null, null);
        }
        Long idUsuario = control.usuarioActual().getIdUsuario();
        return new Actor(false,
                control.esPadre() ? control.idPadreTutorDe(idUsuario) : null,
                control.esJugador() ? control.idJugadorJovenDe(idUsuario) : null);
    }

    /** Solo el padre (o ADMIN) crea, modifica o elimina vinculos. El jugador recibe 403. */
    private void exigirPadre(Actor a) {
        if (a.idPadreTutor() == null) {
            throw new AccesoDenegadoException(control.esJugador() ? MSG_JUGADOR_NO_MODIFICA : MSG_SIN_PERFIL_PADRE);
        }
    }

    /** El padre solo opera a su nombre. */
    private void exigirMismoPadre(Actor a, Long idPadreTutor) {
        if (!a.idPadreTutor().equals(idPadreTutor)) {
            throw new AccesoDenegadoException(MSG_VINCULO_AJENO);
        }
    }

    // ------------------------------------------------------------------ CRUD

    @Override
    public List<PadreHijoDTO> listar() {
        Actor a = actor();
        if (a.admin()) {
            return repository.findAll().stream().map(this::toDTO).toList();
        }
        // Padre: sus vinculos. Jugador: los suyos.
        Map<Long, PadreHijo> propios = new LinkedHashMap<>();
        if (a.idPadreTutor() != null) {
            repository.buscarHijosPorPadre(a.idPadreTutor()).forEach(v -> propios.put(v.getIdPadreHijo(), v));
        }
        if (a.idJugadorJoven() != null) {
            repository.buscarPorJoven(a.idJugadorJoven()).forEach(v -> propios.put(v.getIdPadreHijo(), v));
        }
        return propios.values().stream().map(this::toDTO).toList();
    }

    @Override
    public PadreHijoDTO buscarPorId(Long id) {
        PadreHijo v = buscar(id);
        Actor a = actor();
        if (!a.admin() && !a.esPadreDe(v) && !a.esHijoDe(v)) {
            throw new AccesoDenegadoException(MSG_VER_AJENO);
        }
        return toDTO(v);
    }

    @Override
    public PadreHijoDTO insertar(PadreHijoDTO dto) {
        Actor a = actor();
        if (!a.admin()) {
            exigirPadre(a);
            exigirMismoPadre(a, dto.getIdPadreTutor());
            // Solo ADMIN crea vinculos ya activos: el hijo debe autorizarlo
            dto.setEstado(PENDIENTE);
            dto.setAutorizado(false);
        }
        dto.setIdPadreHijo(null);
        validarNoDuplicado(dto.getIdPadreTutor(), dto.getIdJugadorJoven());
        PadreHijo e = toEntity(dto);
        if (e.getFechaAsociacion() == null) {
            e.setFechaAsociacion(LocalDateTime.now());
        }
        if (e.getEstado() == null) {
            e.setEstado(PENDIENTE);
        }
        if (e.getAutorizado() == null) {
            e.setAutorizado("ACTIVO".equals(e.getEstado()));
        }
        if (e.getPermisosSupervision() == null) {
            e.setPermisosSupervision(PERMISOS_POR_DEFECTO);
        }
        return toDTO(repository.save(e));
    }

    @Override
    public PadreHijoDTO actualizar(Long id, PadreHijoDTO dto) {
        PadreHijo existente = buscar(id);
        Actor a = actor();
        boolean cambiaPar = !existente.getPadreTutor().getIdPadreTutor().equals(dto.getIdPadreTutor())
                || !existente.getJugadorJoven().getIdJugadorJoven().equals(dto.getIdJugadorJoven());
        if (!a.admin()) {
            exigirPadre(a);
            if (!a.esPadreDe(existente)) {
                throw new AccesoDenegadoException(MSG_VINCULO_AJENO);
            }
            if (cambiaPar) {
                throw new AccesoDenegadoException(MSG_NO_CAMBIAR_PAR);
            }
            // estado y autorizado solo los cambia ADMIN (o el hijo con /autorizar)
            dto.setEstado(existente.getEstado());
            dto.setAutorizado(existente.getAutorizado());
        }
        if (cambiaPar) {
            validarNoDuplicado(dto.getIdPadreTutor(), dto.getIdJugadorJoven());
        }
        PadreHijo e = toEntity(dto);
        e.setIdPadreHijo(id);
        if (e.getFechaAsociacion() == null) {
            e.setFechaAsociacion(existente.getFechaAsociacion());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        PadreHijo existente = buscar(id);
        Actor a = actor();
        if (!a.admin()) {
            exigirPadre(a);
            if (!a.esPadreDe(existente)) {
                throw new AccesoDenegadoException(MSG_VINCULO_AJENO);
            }
        }
        repository.deleteById(id);
    }

    @Override
    public List<PadreHijoDTO> buscarPorPadre(Long idPadreTutor) {
        Actor a = actor();
        if (!a.admin() && (a.idPadreTutor() == null || !a.idPadreTutor().equals(idPadreTutor))) {
            throw new AccesoDenegadoException(MSG_VER_AJENO);
        }
        return repository.buscarHijosPorPadre(idPadreTutor).stream().map(this::toDTO).toList();
    }

    @Override
    public List<PadreHijoDTO> buscarPorJoven(Long idJugadorJoven) {
        Actor a = actor();
        if (a.admin() || idJugadorJoven.equals(a.idJugadorJoven())) {
            return repository.buscarPorJoven(idJugadorJoven).stream().map(this::toDTO).toList();
        }
        if (a.idPadreTutor() != null) {
            // Un padre solo ve su propio vinculo con ese joven (no los de otros padres)
            return repository.buscarPorJoven(idJugadorJoven).stream()
                    .filter(a::esPadreDe)
                    .map(this::toDTO).toList();
        }
        throw new AccesoDenegadoException(MSG_VER_AJENO);
    }

    @Override
    @Transactional
    public PadreHijoDTO vincular(VincularDTO dto) {
        Actor a = actor();
        if (!a.admin()) {
            exigirPadre(a);
            exigirMismoPadre(a, dto.getIdPadreTutor());
        }
        PadreTutor padre = buscarPadre(dto.getIdPadreTutor());
        String codigo = dto.getCodigoVinculacion().trim().toUpperCase();
        JugadorJoven joven = jugadorJovenRepository.findByCodigoVinculacion(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un jugador con el código " + codigo));
        validarNoDuplicado(padre.getIdPadreTutor(), joven.getIdJugadorJoven());

        PadreHijo e = new PadreHijo();
        e.setPadreTutor(padre);
        e.setJugadorJoven(joven);
        e.setTipoRelacion(dto.getTipoRelacion() != null ? dto.getTipoRelacion() : "TUTOR");
        e.setFechaAsociacion(LocalDateTime.now());
        e.setEstado(PENDIENTE);
        e.setAutorizado(false);
        e.setPermisosSupervision(PERMISOS_POR_DEFECTO);
        return toDTO(repository.save(e));
    }

    @Override
    @Transactional
    public PadreHijoDTO autorizar(Long id) {
        PadreHijo e = buscar(id);
        Actor a = actor();
        if (!a.admin()) {
            if (!a.esHijoDe(e)) {
                // El padre ya no puede autorizarse a si mismo
                throw new AccesoDenegadoException(a.idPadreTutor() != null ? MSG_SOLO_JUGADOR_AUTORIZA : MSG_VINCULO_AJENO);
            }
            if (!PENDIENTE.equals(e.getEstado())) {
                throw new AccesoDenegadoException(MSG_SOLO_PENDIENTE);
            }
        }
        e.setEstado("ACTIVO");
        e.setAutorizado(true);
        return toDTO(repository.save(e));
    }
}
