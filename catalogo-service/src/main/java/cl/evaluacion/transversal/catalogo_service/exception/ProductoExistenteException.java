package cl.evaluacion.transversal.catalogo_service.exception;

public class ProductoExistenteException extends RuntimeException {
    public ProductoExistenteException(String message) {
        super(message);
    }
}
