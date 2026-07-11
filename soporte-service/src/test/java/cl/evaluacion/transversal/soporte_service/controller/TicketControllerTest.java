package cl.evaluacion.transversal.soporte_service.controller;

import cl.evaluacion.transversal.soporte_service.assembler.TicketModelAssembler;
import cl.evaluacion.transversal.soporte_service.dto.TicketRequest;
import cl.evaluacion.transversal.soporte_service.model.Ticket;
import cl.evaluacion.transversal.soporte_service.service.TicketService;
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
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private TicketService ticketService;


    private TicketModelAssembler ticketModelAssembler;

    private Ticket ticketMock;

    @BeforeEach
    void setUp() {
        ticketMock = Ticket.builder()
                .id(1L)
                .idUsuario(10L)
                .asunto("Consulta de producto")
                .descripcion("¿Tienen stock del producto X?")
                .estado("EN_PROCESO")
                .fechaCreacion(new Date())
                .build();
    }

    @Test
    void testFindAll_DebeRetornar200() throws Exception {
        when(ticketService.findAll()).thenReturn(Arrays.asList(ticketMock));
        when(ticketModelAssembler.toModel(any(Ticket.class)))
                .thenReturn(EntityModel.of(ticketMock));

        mockMvc.perform(get("/api/soporte/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketList[0].asunto").value("Consulta de producto"));
    }

    @Test
    void testFindAll_DebeRetornar404SiVacio() throws Exception {
        when(ticketService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/soporte/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById_DebeRetornar200() throws Exception {
        when(ticketService.findById(1L)).thenReturn(ticketMock);
        when(ticketModelAssembler.toModel(any(Ticket.class)))
                .thenReturn(EntityModel.of(ticketMock));

        mockMvc.perform(get("/api/soporte/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }

    @Test
    void testCreate_DebeRetornar201() throws Exception {
        TicketRequest request = new TicketRequest();
        request.setIdUsuario(10L);
        request.setAsunto("Consulta de producto");
        request.setDescripcion("¿Tienen stock del producto X?");
        request.setEstado("EN_PROCESO");
        request.setFechaCreacion(new Date());

        when(ticketService.save(any(TicketRequest.class))).thenReturn(ticketMock);

        EntityModel<Ticket> entityModel = EntityModel.of(ticketMock, Link.of("/api/soporte/1", "self"));
        when(ticketModelAssembler.toModel(any(Ticket.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/soporte/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descripcion").value("¿Tienen stock del producto X?"));
    }

    @Test
    void testDelete_DebeRetornar204() throws Exception {
        doNothing().when(ticketService).deleteById(1L);

        mockMvc.perform(delete("/api/soporte/1"))
                .andExpect(status().isNoContent());

        verify(ticketService, times(1)).deleteById(1L);
    }

    @Test
    void testFindByEstado_DebeRetornar200() throws Exception {
        when(ticketService.findByEstado("EN_PROCESO")).thenReturn(Arrays.asList(ticketMock));
        when(ticketModelAssembler.toModel(any(Ticket.class)))
                .thenReturn(EntityModel.of(ticketMock));

        mockMvc.perform(get("/api/soporte/estado/EN_PROCESO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketList[0].estado").value("EN_PROCESO"));
    }

    @Test
    void testFindByEstado_DebeRetornar404SiVacio() throws Exception {
        when(ticketService.findByEstado("ABIERTO")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/soporte/estado/ABIERTO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}