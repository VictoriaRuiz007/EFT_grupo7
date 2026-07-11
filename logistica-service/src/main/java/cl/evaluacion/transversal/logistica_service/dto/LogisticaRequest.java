package cl.evaluacion.transversal.logistica_service.dto;

import jakarta.validation.constraints.NotBlank;
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
public class LogisticaRequest {

    @NotBlank(message = "El codigo seguimiento es obligatorio")
    private String codigoSeguimiento;

    @NotBlank(message="La empresa de envio es obligatoria")
    private String empresaEnvio;

    @NotBlank(message = "El estado del envio es obligatorio")
    private String estado; // Ej: "PREPARANDO", "EN_RUTA", "ENTREGADO"

    @NotBlank(message = "La dirección de envío es obligatoria")
    private String direccionDestino;

    @NotNull(message="La fecha de envio es obligatoria")
    private Date fechaEstimada;

    @NotBlank(message = "El envio debe tener un pedido asociado")
    private Long idPedido;


}