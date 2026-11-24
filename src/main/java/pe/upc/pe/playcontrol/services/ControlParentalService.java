package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.entities.PadreHijo;
import pe.upc.pe.playcontrol.entities.User;

import java.util.Set;

/**
 * Reglas de control parental compartidas por limites y vinculos.
 * Los ids "Usuario" son de la tabla usuario; idPadreTutor / idJugadorJoven son de los perfiles.
 */
public interface ControlParentalService {
    String ROLE_ADMIN = "ROLE_ADMIN";
    String ROLE_PADRE = "ROLE_PADRE";
    String ROLE_JUGADOR = "ROLE_JUGADOR";

    /** Usuario autenticado (por username del SecurityContext). 403 si no hay sesion. */
    User usuarioActual();

    /** Roles (authorities) del usuario autenticado. */
    Set<String> rolesActuales();

    boolean esAdmin();
    boolean esPadre();
    boolean esJugador();

    /** true si el usuario tiene el rol indicado (segun rol_usuario). */
    boolean tieneRol(User usuario, String rol);

    /** idPadreTutor del perfil del usuario, o null si no tiene perfil de padre. */
    Long idPadreTutorDe(Long idUsuario);

    /** idJugadorJoven del perfil del usuario, o null si no tiene perfil de jugador. */
    Long idJugadorJovenDe(Long idUsuario);

    /** estado ACTIVO y autorizado = true. */
    boolean esVinculoActivo(PadreHijo vinculo);

    /** Existe PadreHijo con estado ACTIVO y autorizado = true entre ese padre y ese joven (ids de perfil). */
    boolean vinculoActivo(Long idPadreTutor, Long idJugadorJoven);

    /** El padre tiene vinculo activo con el jugador y permisosSupervision contiene CONFIGURAR_LIMITES (ids de usuario). */
    boolean padrePuedeConfigurar(Long idUsuarioPadre, Long idUsuarioJugador);

    /** ids de usuario de los hijos con vinculo activo del padre (id de usuario). */
    Set<Long> hijosActivos(Long idUsuarioPadre);

    /** idJugadorJoven (id de perfil, no de usuario) de los hijos con vinculo activo del padre (id de usuario). */
    Set<Long> jovenesActivos(Long idUsuarioPadre);

    /**
     * El jugador tiene un limite ACTIVO creado por otro usuario con ROLE_PADRE que mantiene vinculo activo con el.
     * Si el vinculo deja de estar ACTIVO, o el limite del padre pasa a INACTIVO, el control vuelve al jugador.
     */
    boolean limiteControladoPorPadre(Long idUsuarioJugador);
}
