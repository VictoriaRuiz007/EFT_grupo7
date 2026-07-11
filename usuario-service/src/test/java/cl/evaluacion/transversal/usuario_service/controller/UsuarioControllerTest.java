package cl.evaluacion.transversal.usuario_service.controller;

import cl.evaluacion.transversal.usuario_service.assembler.UsuarioModelAsssembler;
import cl.evaluacion.transversal.usuario_service.dto.UsuarioRequest;
import cl.evaluacion.transversal.usuario_service.model.Usuario;
import cl.evaluacion.transversal.usuario_service.service.UsuarioService;
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
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private UsuarioService usuarioService;



    private UsuarioModelAsssembler usuarioModelAssembler;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        usuarioMock = Usuario.builder()
                .id(1L)
                .nombre("Ana")
                .apellido("Gómez")
                .correo("ana.gomez@email.com")
                .contrasenia("securepass")
                .rol("CLIENTE")
                .build();
    }

    @Test
    void testFindAll_DebeRetornar200() throws Exception {
        when(usuarioService.findAll()).thenReturn(Arrays.asList(usuarioMock));
        when(usuarioModelAssembler.toModel(any(Usuario.class)))
                .thenReturn(EntityModel.of(usuarioMock));

        mockMvc.perform(get("/api/usuarios/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioList[0].nombre").value("Ana"));
    }

    @Test
    void testFindAll_DebeRetornar204SiVacio() throws Exception {
        when(usuarioService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/usuarios/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById_DebeRetornar200() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuarioMock);
        when(usuarioModelAssembler.toModel(any(Usuario.class)))
                .thenReturn(EntityModel.of(usuarioMock));

        mockMvc.perform(get("/api/usuarios/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("ana.gomez@email.com"));
    }

    @Test
    void testFindByCorreo_DebeRetornar200() throws Exception {
        when(usuarioService.findByCorreo("ana.gomez@email.com")).thenReturn(usuarioMock);
        when(usuarioModelAssembler.toModel(any(Usuario.class)))
                .thenReturn(EntityModel.of(usuarioMock));

        mockMvc.perform(get("/api/usuarios/correo/ana.gomez@email.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void testCreate_DebeRetornar201() throws Exception {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Ana");
        request.setApellido("Gómez");
        request.setCorreo("ana.gomez@email.com");
        request.setContrasenia("securepass");

        when(usuarioService.save(any(UsuarioRequest.class))).thenReturn(usuarioMock);

        EntityModel<Usuario> entityModel = EntityModel.of(usuarioMock, Link.of("/api/usuarios/1", "self"));
        when(usuarioModelAssembler.toModel(any(Usuario.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/usuarios/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void testDelete_DebeRetornar204() throws Exception {
        doNothing().when(usuarioService).deleteById(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deleteById(1L);
    }
}