package cl.evaluacion.transversal.wishlist_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeseoResponse {
    private Long id;
    private Long idUsuario;
    private Long idProducto;
    private Date fechaAgregado;
}