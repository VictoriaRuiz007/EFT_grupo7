package cl.evaluacion.transversal.promociones_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="promociones")
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String codigo;

    @Column(name="porcentaje_descuento", nullable=false)
    private Double porcentajeDescuento;

    @Column(name="fecha_expiracion")
    private Date fechaExpiracion;

    @Column
    private boolean activo;
}
