package cl.evaluacion.transversal.wishlist_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeseoRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;

    @NotNull(message="La fecha de adición es obligatoria")
    private Date fechaAgregado;

}