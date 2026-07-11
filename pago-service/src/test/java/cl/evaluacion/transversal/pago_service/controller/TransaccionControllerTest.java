package cl.evaluacion.transversal.pago_service.controller;

import cl.evaluacion.transversal.pago_service.assembler.TransaccionModelAssembler;
import cl.evaluacion.transversal.pago_service.dto.TransaccionRequest;
import cl.evaluacion.transversal.pago_service.model.Transaccion;
import cl.evaluacion.transversal.pago_service.service.TransaccionService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private TransaccionService transaccionService;


    private TransaccionModelAssembler transaccionModelAssembler;

    private Transaccion transaccionMock;

    @BeforeEach
    void setUp() {
        transaccionMock = Transaccion.builder()
                .id(1L)
                .idPedido(100L)
                .metodoPago("Tarjeta de Débito")
                .monto(15000.0)
                .estado("Pendiente")
                .fecha(LocalDateTime.now())
                .build();
    }

    @Test
    void testFindAll_DebeRetornar200() throws Exception {
        when(transaccionService.findAll()).thenReturn(Arrays.asList(transaccionMock));
        when(transaccionModelAssembler.toModel(any(Transaccion.class)))
                .thenReturn(EntityModel.of(transaccionMock));

        mockMvc.perform(get("/api/pago/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transaccionList[0].metodoPago").value("Tarjeta de Débito"));
    }

    @Test
    void testFindAll_DebeRetornar204SiVacio() throws Exception {
        when(transaccionService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pago/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_DebeRetornar200() throws Exception {
        when(transaccionService.findById(1L)).thenReturn(transaccionMock);
        when(transaccionModelAssembler.toModel(any(Transaccion.class)))
                .thenReturn(EntityModel.of(transaccionMock));

        mockMvc.perform(get("/api/pago/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(15000.0));
    }

    @Test
    void testCrear_DebeRetornar201() throws Exception {
        TransaccionRequest request = new TransaccionRequest();
        request.setIdPedido(100L);
        request.setMetodoPago("Tarjeta de Débito");
        request.setMonto(15000.0);
        request.setEstado("Pendiente");
        request.setFecha(LocalDateTime.now());

        when(transaccionService.save(any(TransaccionRequest.class))).thenReturn(transaccionMock);

        EntityModel<Transaccion> entityModel = EntityModel.of(transaccionMock, Link.of("/api/pago/1", "self"));
        when(transaccionModelAssembler.toModel(any(Transaccion.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/pago/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("Pendiente"));
    }

    @Test
    void testEliminar_DebeRetornar204() throws Exception {
        doNothing().when(transaccionService).deleteById(1L);

        mockMvc.perform(delete("/api/pago/1"))
                .andExpect(status().isNoContent());

        verify(transaccionService, times(1)).deleteById(1L);
    }

    @Test
    void testFindByIdPedido_DebeRetornar200() throws Exception {
        when(transaccionService.findByIdPedido(100L)).thenReturn(transaccionMock);
        when(transaccionModelAssembler.toModel(any(Transaccion.class)))
                .thenReturn(EntityModel.of(transaccionMock));

        mockMvc.perform(get("/api/pago/pedido/100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido").value(100L));
    }
}