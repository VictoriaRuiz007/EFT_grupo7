package cl.evaluacion.transversal.wishlist_service.controller;

import cl.evaluacion.transversal.wishlist_service.assembler.DeseoModelAssembler;
import cl.evaluacion.transversal.wishlist_service.dto.DeseoRequest;
import cl.evaluacion.transversal.wishlist_service.model.Deseo;
import cl.evaluacion.transversal.wishlist_service.service.DeseoService;
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

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeseoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private DeseoService deseoService;


    private DeseoModelAssembler deseoModelAssembler;

    private Deseo deseoMock;

    @BeforeEach
    void setUp() {
        deseoMock = Deseo.builder()
                .id(1L)
                .idUsuario(15L)
                .idProducto(250L)
                .fechaAgregado(new Date())
                .build();
    }

    @Test
    void testFindByIdUsuario_DebeRetornar200() throws Exception {
        when(deseoService.listByUsuario(15L)).thenReturn(deseoMock);
        when(deseoModelAssembler.toModel(any(Deseo.class)))
                .thenReturn(EntityModel.of(deseoMock));

        mockMvc.perform(get("/api/wishlist/usuario/15")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(250L));
    }

    @Test
    void testCreate_DebeRetornar201() throws Exception {
        DeseoRequest request = new DeseoRequest();
        request.setIdUsuario(15L);
        request.setIdProducto(250L);
        request.setFechaAgregado(new Date());

        when(deseoService.save(any(DeseoRequest.class))).thenReturn(deseoMock);

        EntityModel<Deseo> entityModel = EntityModel.of(deseoMock, Link.of("/api/wishlist/usuario/15", "self"));
        when(deseoModelAssembler.toModel(any(Deseo.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/wishlist/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto").value(250L));
    }

    @Test
    void testDelete_DebeRetornar204() throws Exception {
        doNothing().when(deseoService).deleteByUsuario(15L);

        mockMvc.perform(delete("/api/wishlist/usuario/15"))
                .andExpect(status().isNoContent());

        verify(deseoService, times(1)).deleteByUsuario(15L);
    }
}