package cl.evaluacion.transversal.logistica_service.controller;

import cl.evaluacion.transversal.logistica_service.assembler.SeguimientoModelAssembler;
import cl.evaluacion.transversal.logistica_service.dto.LogisticaRequest;
import cl.evaluacion.transversal.logistica_service.model.Seguimiento;
import cl.evaluacion.transversal.logistica_service.service.SeguimientoService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SeguimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private SeguimientoService seguimientoService;


    private SeguimientoModelAssembler seguimientoModelAssembler;

    private Seguimiento seguimientoMock;

    @BeforeEach
    void setUp() {
        seguimientoMock = Seguimiento.builder()
                .id(1L)
                .codigoSeguimiento("TRK-123")
                .empresaEnvio("Chilexpress")
                .estado("Pendiente")
                .direccionDestino("Santiago")
                .fechaEstimada(new Date())
                .idPedido(10L)
                .build();
    }

    @Test
    void testFindAll_DebeRetornar200() throws Exception {
        when(seguimientoService.findAll()).thenReturn(Arrays.asList(seguimientoMock));
        when(seguimientoModelAssembler.toModel(any(Seguimiento.class)))
                .thenReturn(EntityModel.of(seguimientoMock));

        mockMvc.perform(get("/api/seguimiento/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.seguimientoList[0].codigoSeguimiento").value("TRK-123"));
    }

    @Test
    void testFindAll_DebeRetornar204() throws Exception {
        when(seguimientoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/seguimiento/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindByCodigo_DebeRetornar200() throws Exception {
        when(seguimientoService.findByCodigo("TRK-123")).thenReturn(seguimientoMock);
        when(seguimientoModelAssembler.toModel(any(Seguimiento.class)))
                .thenReturn(EntityModel.of(seguimientoMock));

        mockMvc.perform(get("/api/seguimiento/codigo/TRK-123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoSeguimiento").value("TRK-123"));
    }

    @Test
    void testFindById_DebeRetornar200() throws Exception {
        when(seguimientoService.findById(1L)).thenReturn(seguimientoMock);
        when(seguimientoModelAssembler.toModel(any(Seguimiento.class)))
                .thenReturn(EntityModel.of(seguimientoMock));

        mockMvc.perform(get("/api/seguimiento/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoSeguimiento").value("TRK-123"));
    }

    @Test
    void testCreate_DebeRetornar201() throws Exception {
        LogisticaRequest request = new LogisticaRequest();
        request.setCodigoSeguimiento("TRK-123");
        request.setEmpresaEnvio("Chilexpress");
        request.setEstado("Pendiente");
        request.setDireccionDestino("Santiago");
        request.setFechaEstimada(new Date());

        when(seguimientoService.save(any(LogisticaRequest.class))).thenReturn(seguimientoMock);

        EntityModel<Seguimiento> entityModel = EntityModel.of(seguimientoMock, Link.of("/api/seguimiento/1", "self"));
        when(seguimientoModelAssembler.toModel(any(Seguimiento.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/seguimiento/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoSeguimiento").value("TRK-123"));
    }

    @Test
    void testDelete_DebeRetornar204() throws Exception {
        doNothing().when(seguimientoService).deleteById(1L);

        mockMvc.perform(delete("/api/seguimiento/1"))
                .andExpect(status().isNoContent());

        verify(seguimientoService, times(1)).deleteById(1L);
    }
}