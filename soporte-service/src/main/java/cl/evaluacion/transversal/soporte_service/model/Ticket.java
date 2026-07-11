package cl.evaluacion.transversal.soporte_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="tickets_soporte")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="id_usuario", nullable = false)
    private Long idUsuario;

    @Column(nullable=false)
    private String asunto;

    @Column(nullable=false)
    private String descripcion;

    @Column(nullable=false)
    private String estado; //ABIERTO, EN_PROCESO, RESUELTO

    @Column(name="fecha_creacion", nullable=false)
    private Date fechaCreacion;

}
