package pe.upc.pe.playcontrol.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.entities.JugadorJoven;
import pe.upc.pe.playcontrol.entities.LimiteJuego;
import pe.upc.pe.playcontrol.entities.PadreHijo;
import pe.upc.pe.playcontrol.entities.PadreTutor;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.AccesoDenegadoException;
import pe.upc.pe.playcontrol.repositories.JugadorJovenRepository;
import pe.upc.pe.playcontrol.repositories.LimiteJuegoRepository;
import pe.upc.pe.playcontrol.repositories.PadreHijoRepository;
import pe.upc.pe.playcontrol.repositories.PadreTutorRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.ControlParentalService;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ControlParentalServiceImpl implements ControlParentalService {

    private static final String ACTIVO = "ACTIVO";
    private static final String PERMISO_CONFIGURAR_LIMITES = "CONFIGURAR_LIMITES";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PadreTutorRepository padreTutorRepository;
    @Autowired
    private JugadorJovenRepository jugadorJovenRepository;
    @Autowired
    private PadreHijoRepository padreHijoRepository;
    @Autowired
    private LimiteJuegoRepository limiteJuegoRepository;

    private Authentication autenticacion() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new AccesoDenegadoException("Debes iniciar sesión");
        }
        return auth;
    }

    @Override
    public User usuarioActual() {
        String username = autenticacion().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario autenticado no encontrado: " + username));
    }

    @Override
    public Set<String> rolesActuales() {
        return autenticacion().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean esAdmin() {
        return rolesActuales().contains(ROLE_ADMIN);
    }

    @Override
    public boolean esPadre() {
        return rolesActuales().contains(ROLE_PADRE);
    }

    @Override
    public boolean esJugador() {
        return rolesActuales().contains(ROLE_JUGADOR);
    }

    @Override
    public boolean tieneRol(User usuario, String rol) {
        return usuario != null && usuario.getRoles() != null
                && usuario.getRoles().stream().anyMatch(r -> rol.equals(r.getAuthority()));
    }

    @Override
    public Long idPadreTutorDe(Long idUsuario) {
        if (idUsuario == null) {
            return null;
        }
        return padreTutorRepository.findByUsuario_IdUsuario(idUsuario).map(PadreTutor::getIdPadreTutor).orElse(null);
    }

    @Override
    public Long idJugadorJovenDe(Long idUsuario) {
        if (idUsuario == null) {
            return null;
        }
        return jugadorJovenRepository.findByUsuario_IdUsuario(idUsuario).map(JugadorJoven::getIdJugadorJoven).orElse(null);
    }

    @Override
    public boolean esVinculoActivo(PadreHijo vinculo) {
        return vinculo != null && ACTIVO.equals(vinculo.getEstado()) && Boolean.TRUE.equals(vinculo.getAutorizado());
    }

    private Optional<PadreHijo> buscarVinculoActivo(Long idPadreTutor, Long idJugadorJoven) {
        if (idPadreTutor == null || idJugadorJoven == null) {
            return Optional.empty();
        }
        return padreHijoRepository.findByPadreTutor_IdPadreTutorAndJugadorJoven_IdJugadorJoven(idPadreTutor, idJugadorJoven)
                .filter(this::esVinculoActivo);
    }

    @Override
    public boolean vinculoActivo(Long idPadreTutor, Long idJugadorJoven) {
        return buscarVinculoActivo(idPadreTutor, idJugadorJoven).isPresent();
    }

    @Override
    public boolean padrePuedeConfigurar(Long idUsuarioPadre, Long idUsuarioJugador) {
        return buscarVinculoActivo(idPadreTutorDe(idUsuarioPadre), idJugadorJovenDe(idUsuarioJugador))
                .map(v -> tienePermiso(v.getPermisosSupervision(), PERMISO_CONFIGURAR_LIMITES))
                .orElse(false);
    }

    private boolean tienePermiso(String permisos, String permiso) {
        return permisos != null && Arrays.stream(permisos.split(",")).map(String::trim).anyMatch(permiso::equals);
    }

    @Override
    public Set<Long> hijosActivos(Long idUsuarioPadre) {
        Long idPadreTutor = idPadreTutorDe(idUsuarioPadre);
        if (idPadreTutor == null) {
            return Set.of();
        }
        return padreHijoRepository.buscarHijosPorPadre(idPadreTutor).stream()
                .filter(this::esVinculoActivo)
                .map(v -> v.getJugadorJoven().getUsuario().getIdUsuario())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Long> jovenesActivos(Long idUsuarioPadre) {
        Long idPadreTutor = idPadreTutorDe(idUsuarioPadre);
        if (idPadreTutor == null) {
            return Set.of();
        }
        return padreHijoRepository.buscarHijosPorPadre(idPadreTutor).stream()
                .filter(this::esVinculoActivo)
                .map(v -> v.getJugadorJoven().getIdJugadorJoven())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean limiteControladoPorPadre(Long idUsuarioJugador) {
        Long idJugadorJoven = idJugadorJovenDe(idUsuarioJugador);
        if (idJugadorJoven == null) {
            return false;
        }
        for (LimiteJuego limite : limiteJuegoRepository.buscarActivosPorUsuario(idUsuarioJugador)) {
            User creador = limite.getCreadoPor();
            if (creador == null || creador.getIdUsuario().equals(idUsuarioJugador) || !tieneRol(creador, ROLE_PADRE)) {
                continue;
            }
            if (vinculoActivo(idPadreTutorDe(creador.getIdUsuario()), idJugadorJoven)) {
                return true;
            }
        }
        return false;
    }
}
