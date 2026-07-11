package cl.evaluacion.transversal.promociones_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromocionRequest {

    @NotBlank(message = "El código de la promoción es obligatorio")
    private String codigo;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @DecimalMin(value="0.0", inclusive =false, message="Descuento no puede ser 0")
    @DecimalMax(value="1,0", message="El descuento no puede ser de 100%")
    private Double porcentajeDescuento;

    @Future(message="La fecha de expiracion debe ser futura")
    private Date fechaExpiracion;

    private boolean activo;
}