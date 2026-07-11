package cl.evaluacion.transversal.wishlist_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="lista_deseos")
public class Deseo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_usuario", nullable=false, unique=true)
    private Long idUsuario;

    @Column(name="id_producto", nullable=false)
    private Long idProducto;

    @Column(name="fecha_agregado", nullable=false)
    private Date fechaAgregado;
}
