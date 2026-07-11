package cl.evaluacion.transversal.promociones_service.controller;

import cl.evaluacion.transversal.promociones_service.assembler.PromocionModelAssembler;
import cl.evaluacion.transversal.promociones_service.dto.PromocionRequest;
import cl.evaluacion.transversal.promociones_service.model.Promocion;
import cl.evaluacion.transversal.promociones_service.service.PromocionService;
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
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private PromocionService promocionService;


    private PromocionModelAssembler promocionModelAssembler;

    private Promocion promocionMock;

    @BeforeEach
    void setUp() {
        promocionMock = Promocion.builder()
                .id(1L)
                .codigo("INVIERNO15")
                .porcentajeDescuento(15.0)
                .fechaExpiracion(new Date())
                .activo(true)
                .build();
    }

    @Test
    void testFindAll_DebeRetornar200() throws Exception {
        when(promocionService.findAll()).thenReturn(Arrays.asList(promocionMock));
        when(promocionModelAssembler.toModel(any(Promocion.class)))
                .thenReturn(EntityModel.of(promocionMock));

        mockMvc.perform(get("/api/promociones/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.promocionList[0].codigo").value("INVIERNO15"));
    }

    @Test
    void testFindAll_DebeRetornar204SiVacio() throws Exception {
        when(promocionService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/promociones/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_DebeRetornar200() throws Exception {
        when(promocionService.findById(1L)).thenReturn(promocionMock);
        when(promocionModelAssembler.toModel(any(Promocion.class)))
                .thenReturn(EntityModel.of(promocionMock));

        mockMvc.perform(get("/api/promociones/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.porcentajeDescuento").value(15.0));
    }

    @Test
    void testFindByCodigo_DebeRetornar200() throws Exception {
        when(promocionService.findByCodigo("INVIERNO15")).thenReturn(promocionMock);
        when(promocionModelAssembler.toModel(any(Promocion.class)))
                .thenReturn(EntityModel.of(promocionMock));

        mockMvc.perform(get("/api/promociones/codigo/INVIERNO15")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("INVIERNO15"));
    }

    @Test
    void testCreate_DebeRetornar201() throws Exception {
        PromocionRequest request = new PromocionRequest();
        request.setCodigo("INVIERNO15");
        request.setPorcentajeDescuento(15.0);
        request.setFechaExpiracion(new Date());

        when(promocionService.save(any(PromocionRequest.class))).thenReturn(promocionMock);

        EntityModel<Promocion> entityModel = EntityModel.of(promocionMock, Link.of("/api/promociones/1", "self"));
        when(promocionModelAssembler.toModel(any(Promocion.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/promociones/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("INVIERNO15"));
    }

    @Test
    void testDelete_DebeRetornar204() throws Exception {
        doNothing().when(promocionService).deleteById(1L);

        mockMvc.perform(delete("/api/promociones/1"))
                .andExpect(status().isNoContent());

        verify(promocionService, times(1)).deleteById(1L);
    }
}
