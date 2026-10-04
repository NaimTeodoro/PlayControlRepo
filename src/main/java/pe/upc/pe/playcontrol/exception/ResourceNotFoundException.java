package pe.upc.pe.playcontrol.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String recurso, Object id) {
        super(recurso + " no encontrado con id " + id);
    }
}
