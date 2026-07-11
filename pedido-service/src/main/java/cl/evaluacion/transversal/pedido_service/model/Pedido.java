package cl.evaluacion.transversal.pedido_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_usuario", nullable=false)
    private Long idUsuario;

    @Column(nullable=false)
    private Double total;

    @Column
    private String codigoPromocion;

    @Column(nullable=false)
    private String detalleProductos;

    @Column(nullable=false)
    private Date fechaPedido;

}
