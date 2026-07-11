package cl.evaluacion.transversal.notificacion_service.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        LocalDateTime fecha,
        int estado,
        String error,
        List<String> mensajes,
        String ruta
) {
}
