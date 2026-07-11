package cl.evaluacion.transversal.faq_service.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="preguntas_frecuentes")
public class FAQ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String pregunta;

    @Column(nullable=false)
    private String respuesta;

    @Column(nullable=false)
    private String categoria;
}
