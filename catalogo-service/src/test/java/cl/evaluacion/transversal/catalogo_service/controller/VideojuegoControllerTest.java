package cl.evaluacion.transversal.catalogo_service.controller;

import cl.evaluacion.transversal.catalogo_service.assembler.VideojuegoModelAssembler;
import cl.evaluacion.transversal.catalogo_service.dto.VideojuegoRequest;
import cl.evaluacion.transversal.catalogo_service.model.Videojuego;
import cl.evaluacion.transversal.catalogo_service.service.VideojuegoService;
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
class VideojuegoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private VideojuegoService videojuegoService;


    private VideojuegoModelAssembler videojuegoModelAssembler;

    private Videojuego videojuegoMock;

    @BeforeEach
    void setUp() {
        videojuegoMock = Videojuego.builder()
                .id(1L)
                .titulo("Halo Infinite")
                .plataforma("Xbox")
                .stock(10)
                .categoria("Shooter")
                .precio(59.99)
                .build();
    }

    @Test
    void testListarTodos_DebeRetornar200YLista() throws Exception {
        when(videojuegoService.listAll()).thenReturn(Arrays.asList(videojuegoMock));
        when(videojuegoModelAssembler.toModel(any(Videojuego.class)))
                .thenReturn(EntityModel.of(videojuegoMock));

        mockMvc.perform(get("/api/catalogo/")
                        .accept(MediaType.parseMediaType("application/hal+json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.videojuegoList[0].titulo").value("Halo Infinite"));
    }

    @Test
    void testListarTodos_DebeRetornar204SiVacio() throws Exception {
        when(videojuegoService.listAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/catalogo/")
                        .accept(MediaType.parseMediaType("application/hal+json")))
                .andExpect(status().isNoContent());
    }

    @Test
    void testBuscarPorId_DebeRetornar200() throws Exception {
        when(videojuegoService.findById(1L)).thenReturn(videojuegoMock);
        when(videojuegoModelAssembler.toModel(any(Videojuego.class)))
                .thenReturn(EntityModel.of(videojuegoMock));

        mockMvc.perform(get("/api/catalogo/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Halo Infinite"));
    }

    @Test
    void testCrear_DebeRetornar201() throws Exception {
        VideojuegoRequest request = new VideojuegoRequest();
        request.setTitulo("Halo Infinite");
        request.setPlataforma("Xbox");
        request.setStock(10);
        request.setCategoria("Shooter");
        request.setPrecio(59.99);

        when(videojuegoService.save(any(VideojuegoRequest.class))).thenReturn(videojuegoMock);

        // Simulamos la respuesta de HATEOAS incluyendo un link para evitar errores de NullPointerException
        EntityModel<Videojuego> entityModel = EntityModel.of(videojuegoMock, Link.of("/api/catalogo/1", "self"));
        when(videojuegoModelAssembler.toModel(any(Videojuego.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/catalogo/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Halo Infinite"));
    }

    @Test
    void testEliminar_DebeRetornar204() throws Exception {
        doNothing().when(videojuegoService).delete(1L);

        mockMvc.perform(delete("/api/catalogo/1"))
                .andExpect(status().isNoContent());

        verify(videojuegoService, times(1)).delete(1L);
    }
}