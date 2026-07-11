package cl.evaluacion.transversal.pedido_service.controller;

import cl.evaluacion.transversal.pedido_service.assembler.PedidoModelAssembler;
import cl.evaluacion.transversal.pedido_service.dto.PedidoRequest;
import cl.evaluacion.transversal.pedido_service.model.Pedido;
import cl.evaluacion.transversal.pedido_service.service.PedidoService;
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
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private PedidoService pedidoService;


    private PedidoModelAssembler pedidoModelAssembler;

    private Pedido pedidoMock;

    @BeforeEach
    void setUp() {
        pedidoMock = Pedido.builder()
                .id(1L)
                .idUsuario(15L)
                .total(20000.0)
                .codigoPromocion("FREESHIP")
                .detalleProductos("Mouse Gamer")
                .fechaPedido(new Date())
                .build();
    }

    @Test
    void testFindAll_DebeRetornar200() throws Exception {
        when(pedidoService.findAll()).thenReturn(Arrays.asList(pedidoMock));
        when(pedidoModelAssembler.toModel(any(Pedido.class)))
                .thenReturn(EntityModel.of(pedidoMock));

        mockMvc.perform(get("/api/pedidos/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidoList[0].total").value(20000.0));
    }

    @Test
    void testFindAll_DebeRetornar404SiVacio() throws Exception {
        // Tu controlador devuelve 404 en lugar de 204
        when(pedidoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/pedidos/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById_DebeRetornar200() throws Exception {
        when(pedidoService.findById(1L)).thenReturn(pedidoMock);
        when(pedidoModelAssembler.toModel(any(Pedido.class)))
                .thenReturn(EntityModel.of(pedidoMock));

        mockMvc.perform(get("/api/pedidos/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoPromocion").value("FREESHIP"));
    }

    @Test
    void testCreate_DebeRetornar201() throws Exception {
        PedidoRequest request = new PedidoRequest();
        request.setIdUsuario(15L);
        request.setTotal(20000.0);
        request.setCodigoPromocion("FREESHIP");
        request.setDetalleProductos("Mouse Gamer");
        request.setFechaPedido(new Date());

        when(pedidoService.create(any(PedidoRequest.class))).thenReturn(pedidoMock);

        EntityModel<Pedido> entityModel = EntityModel.of(pedidoMock, Link.of("/api/pedidos/1", "self"));
        when(pedidoModelAssembler.toModel(any(Pedido.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/pedidos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.detalleProductos").value("Mouse Gamer"));
    }

    @Test
    void testDelete_DebeRetornar204() throws Exception {
        doNothing().when(pedidoService).deleteById(1L);

        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isNoContent());

        verify(pedidoService, times(1)).deleteById(1L);
    }
}