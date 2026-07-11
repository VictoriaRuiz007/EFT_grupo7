package cl.evaluacion.transversal.pago_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionRequest {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long idPedido;

    @NotBlank(message="El metodo de pago es obligatorio")
    private String metodoPago;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotNull(message = "El estado es obligatorio(aprobado, rechazado, pendiente")
    private String estado;

    @NotBlank(message = "La fecha es obligatoria")
    private LocalDateTime fecha;


}