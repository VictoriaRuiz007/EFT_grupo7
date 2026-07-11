package cl.evaluacion.transversal.usuario_service.exception;

public class UsuarioExisteException extends RuntimeException {
    public UsuarioExisteException(String message) {
        super(message);
    }
}
