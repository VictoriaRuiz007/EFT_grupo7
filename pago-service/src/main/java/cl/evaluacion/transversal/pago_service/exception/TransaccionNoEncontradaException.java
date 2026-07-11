package cl.evaluacion.transversal.pago_service.exception;

public class TransaccionNoEncontradaException extends RuntimeException {
    public TransaccionNoEncontradaException(String message) {
        super(message);
    }
}
