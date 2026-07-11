package cl.evaluacion.transversal.logistica_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="despachos")
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="codigo_seguimiento", nullable = false, unique = true)
    private String codigoSeguimiento;

    @Column(name="empresa_envio", nullable=false)
    private String empresaEnvio;

    @Column(nullable=false)
    private String estado;

    @Column(nullable=false)
    private String direccionDestino;

    @Column(nullable=false)
    private Date fechaEstimada;

    @Column(unique=true, nullable=false)
    private Long idPedido;
}
