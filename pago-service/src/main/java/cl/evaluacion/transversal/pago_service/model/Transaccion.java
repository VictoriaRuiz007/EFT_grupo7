package cl.evaluacion.transversal.pago_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="pagos")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_pedido", nullable=false)
    private Long idPedido;

    @Column(name="metodo", nullable=false)
    private String metodoPago;

    @Column(nullable=false)
    private Double monto;

    @Column(nullable=false)
    private String estado;

    @Column(nullable=false)
    private LocalDateTime fecha;
}
