package cl.evaluacion.transversal.promociones_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromocionResponse {
    private Long id;
    private String codigo;
    private Double porcentajeDescuento;
    private Date fechaExpiracion;
    private boolean activo;
}