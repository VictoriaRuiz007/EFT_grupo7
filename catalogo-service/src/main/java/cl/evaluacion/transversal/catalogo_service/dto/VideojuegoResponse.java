package cl.evaluacion.transversal.catalogo_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideojuegoResponse {
    private Long id;
    private String titulo;
    private String plataforma;
    private Integer stock;
    private String categoria;
    private Double precio;
}