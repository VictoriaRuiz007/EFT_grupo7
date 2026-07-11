package cl.evaluacion.transversal.logistica_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogisticaResponse {
    private Long id;
    private String codigoSeguimiento;
    private String empresaEnvio;
    private String estado;
    private String direccionEnvio;
    private Date fechaEstimada;
    private Long idPedido;
}