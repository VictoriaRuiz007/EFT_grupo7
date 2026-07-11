package cl.evaluacion.transversal.pago_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponse {
    private Long id;
    private Long idPedido;
    private String metodoPago;
    private Double monto;
    private String estado;
    private LocalDateTime fecha;
}