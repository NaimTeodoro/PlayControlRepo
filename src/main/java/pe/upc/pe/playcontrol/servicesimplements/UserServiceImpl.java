package pe.upc.pe.playcontrol.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.UserRegisterDTO;
import pe.upc.pe.playcontrol.dtos.UsuarioActualDTO;
import pe.upc.pe.playcontrol.dtos.UsuarioDTO;
import pe.upc.pe.playcontrol.entities.Authority;
import pe.upc.pe.playcontrol.entities.JugadorJoven;
import pe.upc.pe.playcontrol.entities.PadreTutor;
import pe.upc.pe.playcontrol.entities.ProfesionalSalud;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.JugadorJovenRepository;
import pe.upc.pe.playcontrol.repositories.PadreTutorRepository;
import pe.upc.pe.playcontrol.repositories.ProfesionalSaludRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.JugadorJovenService;
import pe.upc.pe.playcontrol.services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    // ROLE_ADMIN NO se puede elegir desde el registro publico.
    private static final Set<String> ROLES_REGISTRO = Set.of("ROLE_JUGADOR", "ROLE_PADRE", "ROLE_PROFESIONAL");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JugadorJovenRepository jugadorJovenRepository;
    @Autowired
    private PadreTutorRepository padreTutorRepository;
    @Autowired
    private ProfesionalSaludRepository profesionalSaludRepository;
    @Autowired
    private JugadorJovenService jugadorJovenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ------------------------------------------------------------------ mapeo (manual: roles es List<Authority>)

    private UsuarioDTO toDTO(User u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setUsername(u.getUsername());
        dto.setEnabled(u.getEnabled());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setEmail(u.getEmail());
        dto.setTelefono(u.getTelefono());
        dto.setFechaNacimiento(u.getFechaNacimiento());
        dto.setGenero(u.getGenero());
        dto.setEstado(u.getEstado());
        dto.setFechaRegistro(u.getFechaRegistro());
        dto.setUltimoAcceso(u.getUltimoAcceso());
        dto.setPais(u.getPais());
        dto.setCiudad(u.getCiudad());
        dto.setDireccion(u.getDireccion());
        dto.setNivelTecnologia(u.getNivelTecnologia());
        dto.setConfiguracionPrivacidad(u.getConfiguracionPrivacidad());
        dto.setRoles(roles(u));
        return dto;
    }

    private void aplicar(UsuarioDTO dto, User u) {
        u.setUsername(dto.getUsername());
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setTelefono(dto.getTelefono());
        u.setFechaNacimiento(dto.getFechaNacimiento());
        u.setGenero(dto.getGenero());
        u.setPais(dto.getPais());
        u.setCiudad(dto.getCiudad());
        u.setDireccion(dto.getDireccion());
        u.setNivelTecnologia(dto.getNivelTecnologia());
        u.setConfiguracionPrivacidad(dto.getConfiguracionPrivacidad());
        if (dto.getEnabled() != null) {
            u.setEnabled(dto.getEnabled());
        }
        if (dto.getEstado() != null) {
            u.setEstado(dto.getEstado());
        }
    }

    private static List<String> roles(User u) {
        return u.getRoles() == null ? List.of() : u.getRoles().stream().map(Authority::getAuthority).toList();
    }

    private static List<Authority> authorities(List<String> roles) {
        List<Authority> lista = new ArrayList<>();
        for (String r : roles) {
            Authority a = new Authority();
            a.setAuthority(r);
            lista.add(a);
        }
        return lista;
    }

    private User buscar(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    // ------------------------------------------------------------------ CRUD (solo ADMIN)

    @Override
    public List<UsuarioDTO> listar() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    @Transactional
    public UsuarioDTO insertar(UsuarioDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("El usuario ya existe");
        }
        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("El email ya está registrado");
        }
        List<String> roles = (dto.getRoles() == null || dto.getRoles().isEmpty()) ? List.of("ROLE_JUGADOR") : dto.getRoles();

        User u = new User();
        aplicar(dto, u);
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (u.getEnabled() == null) {
            u.setEnabled(true);
        }
        if (u.getEstado() == null) {
            u.setEstado("ACTIVO");
        }
        u.setFechaRegistro(LocalDateTime.now());
        u.setRoles(authorities(roles));
        u = userRepository.save(u);
        for (String rol : roles) {
            crearPerfil(u, rol);
        }
        return toDTO(u);
    }

    @Override
    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        User u = buscar(id);
        if (!u.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("El usuario ya existe");
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(u.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("El email ya está registrado");
        }
        aplicar(dto, u);
        // La contraseña solo cambia si se envia
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            u.getRoles().clear();
            u.getRoles().addAll(authorities(dto.getRoles()));
            for (String rol : dto.getRoles()) {
                crearPerfil(u, rol);
            }
        }
        return toDTO(userRepository.save(u));
    }

    @Override
    public void eliminar(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        userRepository.deleteById(id);
    }

    // ------------------------------------------------------------------ registro publico con perfil

    @Override
    @Transactional
    public Map<String, Object> registrar(UserRegisterDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        String rol = (dto.getRol() == null || dto.getRol().isBlank()) ? "ROLE_JUGADOR" : dto.getRol().trim();
        if (!ROLES_REGISTRO.contains(rol)) {
            throw new IllegalArgumentException("Rol no permitido. Use: " + ROLES_REGISTRO);
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail() == null || dto.getEmail().isBlank() ? null : dto.getEmail());
        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setEnabled(true);
        user.setEstado("ACTIVO");
        user.setFechaRegistro(LocalDateTime.now());
        user.setRoles(authorities(List.of(rol)));
        User guardado = userRepository.save(user);
        crearPerfil(guardado, rol);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje", "Usuario registrado exitosamente");
        respuesta.put("idUsuario", guardado.getIdUsuario());
        respuesta.put("rol", rol);
        return respuesta;
    }

    /** Crea el perfil que corresponde al rol si el usuario aun no lo tiene. */
    private void crearPerfil(User u, String rol) {
        LocalDateTime ahora = LocalDateTime.now();
        switch (rol) {
            case "ROLE_JUGADOR" -> {
                if (jugadorJovenRepository.findByUsuario_IdUsuario(u.getIdUsuario()).isEmpty()) {
                    JugadorJoven j = new JugadorJoven();
                    j.setUsuario(u);
                    j.setCodigoVinculacion(jugadorJovenService.generarCodigoVinculacion());
                    j.setFechaRegistroSegmento(ahora);
                    jugadorJovenRepository.save(j);
                }
            }
            case "ROLE_PADRE" -> {
                if (padreTutorRepository.findByUsuario_IdUsuario(u.getIdUsuario()).isEmpty()) {
                    PadreTutor p = new PadreTutor();
                    p.setUsuario(u);
                    p.setFechaRegistro(ahora);
                    padreTutorRepository.save(p);
                }
            }
            case "ROLE_PROFESIONAL" -> {
                if (profesionalSaludRepository.findByUsuario_IdUsuario(u.getIdUsuario()).isEmpty()) {
                    ProfesionalSalud p = new ProfesionalSalud();
                    p.setUsuario(u);
                    p.setEspecialidad("Por completar");
                    p.setNumeroLicencia("PENDIENTE-" + u.getIdUsuario());
                    p.setEstado("PENDIENTE");
                    profesionalSaludRepository.save(p);
                }
            }
            default -> {
                // ROLE_ADMIN no tiene perfil
            }
        }
    }

    // ------------------------------------------------------------------ /auth/me

    @Override
    public UsuarioActualDTO obtenerActual(String username) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        Long id = u.getIdUsuario();
        return new UsuarioActualDTO(
                id,
                u.getUsername(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                roles(u),
                jugadorJovenRepository.findByUsuario_IdUsuario(id).map(JugadorJoven::getIdJugadorJoven).orElse(null),
                padreTutorRepository.findByUsuario_IdUsuario(id).map(PadreTutor::getIdPadreTutor).orElse(null),
                profesionalSaludRepository.findByUsuario_IdUsuario(id).map(ProfesionalSalud::getIdProfesional).orElse(null));
    }
}
