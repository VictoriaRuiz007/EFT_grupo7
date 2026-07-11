package cl.evaluacion.transversal.notificacion_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionResponse {
    private Long id;
    private Long idUsuario;
    private String tipo;
    private String mensaje;
    private LocalDateTime fechaEnvio;

}