package cl.evaluacion.transversal.soporte_service.dto;

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
public class TicketRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "El asunto del ticket es obligatorio")
    private String asunto;

    @NotBlank(message = "La descripción del problema es obligatoria")
    private String descripcion;

    @NotBlank(message="El estado es obligatorio(ABIERTO; EN PROCESO; RESUELTO)")
    private String estado;

    @NotNull(message="La fecha de creacion es obligatoria")
    private Date fechaCreacion;
}