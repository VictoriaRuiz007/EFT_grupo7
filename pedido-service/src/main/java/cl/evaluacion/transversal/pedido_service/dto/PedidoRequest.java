package cl.evaluacion.transversal.pedido_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El total es obligatorio")
    @Positive(message="El total debe ser mayor a 0")
    private Double total;

    private String codigoPromocion;

    @NotBlank(message="El detalle productos es obligatorio")
    private String detalleProductos;

    @NotNull(message="La fecha es obligatoria")
    private Date fechaPedido;
}