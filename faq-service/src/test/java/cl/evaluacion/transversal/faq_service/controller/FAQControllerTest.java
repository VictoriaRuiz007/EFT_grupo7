package cl.evaluacion.transversal.faq_service.controller;

import cl.evaluacion.transversal.faq_service.assembler.FAQModelAssembler;
import cl.evaluacion.transversal.faq_service.dto.FAQRequest;
import cl.evaluacion.transversal.faq_service.model.FAQ;
import cl.evaluacion.transversal.faq_service.service.FAQService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FAQControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private FAQService faqService;


    private FAQModelAssembler faqModelAssembler;

    private FAQ faqMock;

    @BeforeEach
    void setUp() {
        faqMock = FAQ.builder()
                .id(1L)
                .pregunta("¿Dónde están ubicados?")
                .respuesta("En Santiago de Chile.")
                .categoria("Ubicación")
                .build();
    }

    @Test
    void testFindAll_DebeRetornar200YLista() throws Exception {
        when(faqService.findAll()).thenReturn(Arrays.asList(faqMock));
        when(faqModelAssembler.toModel(any(FAQ.class)))
                .thenReturn(EntityModel.of(faqMock));

        mockMvc.perform(get("/api/faq/")
                        .accept(MediaType.APPLICATION_JSON)) // Puede ser hal+json si está configurado
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.fAQList[0].pregunta").value("¿Dónde están ubicados?"));
    }

    @Test
    void testFindAll_DebeRetornar204SiVacio() throws Exception {
        when(faqService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/faq/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSave_DebeRetornar201() throws Exception {
        FAQRequest request = new FAQRequest();
        request.setPregunta("¿Dónde están ubicados?");
        request.setRespuesta("En Santiago de Chile.");
        request.setCategoria("Ubicación");

        when(faqService.create(any(FAQRequest.class))).thenReturn(faqMock);

        // Simular HATEOAS con self link
        EntityModel<FAQ> entityModel = EntityModel.of(faqMock, Link.of("/api/faq/1", "self"));
        when(faqModelAssembler.toModel(any(FAQ.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/faq")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pregunta").value("¿Dónde están ubicados?"));
    }

    @Test
    void testDelete_DebeRetornar204() throws Exception {
        doNothing().when(faqService).delete(1L);

        mockMvc.perform(delete("/api/faq/1"))
                .andExpect(status().isNoContent());

        verify(faqService, times(1)).delete(1L);
    }
}