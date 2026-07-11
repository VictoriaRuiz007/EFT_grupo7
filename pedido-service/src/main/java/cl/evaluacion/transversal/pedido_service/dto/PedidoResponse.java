package cl.evaluacion.transversal.pedido_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private Long idUsuario;
    private Double total;
    private String codigoPromocion;
    private String detalleProducto;
    private Date fechaPedido;
}