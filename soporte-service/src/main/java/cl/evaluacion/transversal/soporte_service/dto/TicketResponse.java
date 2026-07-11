package cl.evaluacion.transversal.soporte_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private Long idUsuario;
    private String asunto;
    private String descripcion;
    private String estado;
    private Date fechaCreacion;
}