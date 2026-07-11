package cl.evaluacion.transversal.catalogo_service.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="videojuegos")
public class Videojuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String titulo;

    @Column(nullable=false)
    private String plataforma;

    @Column(nullable=false)
    private Integer stock;

    @Column(nullable=false)
    private String categoria;

    @Column(nullable=false)
    private Double precio;
}
