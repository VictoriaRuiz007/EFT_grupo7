package cl.evaluacion.transversal.faq_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FAQRequest {

    @NotBlank(message = "La pregunta es obligatoria")
    private String pregunta;

    @NotBlank(message = "La respuesta es obligatoria")
    private String respuesta;

    @NotBlank(message = "Debes asignarle una categoria")
    private String categoria;
}