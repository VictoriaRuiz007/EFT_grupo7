package cl.evaluacion.transversal.faq_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FAQResponse {
    private Long id;
    private String pregunta;
    private String respuesta;
    private String categoria;
}