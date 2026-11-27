package pe.upc.pe.playcontrol.dtos;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Cita con un profesional de salud, de SOLO LECTURA para jugador, padre y admin.
 * Es una vista reducida de ProfesionalCliente: NO incluye diagnostico, planTratamiento, notas,
 * numeroLicencia ni ningun otro dato clinico o de verificacion. Se arma a mano en CitaServiceImpl
 * (sin ModelMapper) para que un campo nuevo de la entidad nunca se filtre por accidente.
 * proximaConsulta puede ser null (el front muestra "Sin fecha programada").
 */
@Data
public class CitaDTO {
    // Relacion profesional - jugador
    private Long idProfesionalCliente;
    private String estado;
    private String frecuenciaConsulta;
    private LocalDateTime proximaConsulta;

    // Jugador
    private Long idJugadorJoven;
    private Long idUsuarioJugador;
    private String nombreJugador;
    private String apellidoJugador;

    // Profesional
    private Long idProfesional;
    private String nombreProfesional;
    private String apellidoProfesional;
    private String especialidad;
    private String institucion;
    private String telefonoConsulta;
    private String horarioConsulta;
}
