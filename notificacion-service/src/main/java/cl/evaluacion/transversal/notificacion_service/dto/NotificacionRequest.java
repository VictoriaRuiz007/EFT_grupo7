package cl.evaluacion.transversal.notificacion_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El tipo de notificación es obligatorio (pago, pedido, soporte)")
    private String tipo;

    @NotBlank(message = "El mensaje de la notificación no puede estar vacío")
    private String mensaje;

    @NotNull(message = "La fecha de envio es obligatoria")
    private LocalDateTime fechaEnvio;


}