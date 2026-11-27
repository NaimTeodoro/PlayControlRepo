package pe.upc.pe.playcontrol.exception;

/**
 * Error de negocio por conflicto con el estado actual (dato duplicado, transicion no permitida...). Se responde 409.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String mensaje) {
        super(mensaje);
    }
}
