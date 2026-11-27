package pe.upc.pe.playcontrol.exception;

/**
 * Regla de negocio de propiedad o control parental incumplida (limite ajeno, limite controlado por el padre,
 * vinculo ajeno...). Se responde 403 con ProblemDetail. No confundir con el 403 de Spring Security (rol sin permiso).
 */
public class AccesoDenegadoException extends RuntimeException {
    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
