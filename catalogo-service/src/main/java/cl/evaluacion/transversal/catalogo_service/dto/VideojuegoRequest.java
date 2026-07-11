package cl.evaluacion.transversal.catalogo_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideojuegoRequest {

    @NotBlank(message = "El título del producto es obligatorio")
    private String titulo;

    @NotBlank(message= "La plataforma es obligatoria")
    private String plataforma;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotBlank(message="La categoria es obligatoria")
    private String categoria;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;
}