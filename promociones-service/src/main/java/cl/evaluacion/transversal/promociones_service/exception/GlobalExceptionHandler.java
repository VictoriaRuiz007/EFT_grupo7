package cl.evaluacion.transversal.promociones_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PromocionNoEncontradaException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(PromocionNoEncontradaException ex, HttpServletRequest request){
        ApiError error = new ApiError(LocalDateTime.now(),404, "NO ENCONTRADO", List.of(ex.getMessage()), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PromocionExisteException.class)
    public ResponseEntity<ApiError> manejarExistente(PromocionExisteException ex, HttpServletRequest request){
        ApiError error = new ApiError(LocalDateTime.now(), 409, "YA EXISTE", List.of(ex.getMessage()), request.getRequestURI() );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorGeneral (Exception ex, HttpServletRequest request){
        ApiError error = new ApiError(LocalDateTime.now(), 500, "ERROR", List.of(ex.getMessage()), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
