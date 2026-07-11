package cl.evaluacion.transversal.notificacion_service.controller;

import cl.evaluacion.transversal.notificacion_service.assembler.NotificacionModelAssembler;
import cl.evaluacion.transversal.notificacion_service.dto.NotificacionRequest;
import cl.evaluacion.transversal.notificacion_service.model.Notificacion;
import cl.evaluacion.transversal.notificacion_service.service.NotificacionService;
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
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private NotificacionService notificacionService;


    private NotificacionModelAssembler notificacionModelAssembler;

    private Notificacion notificacionMock;

    @BeforeEach
    void setUp() {
        notificacionMock = Notificacion.builder()
                .id(1L)
                .idUsuario(10L)
                .tipo("SMS")
                .mensaje("Código de verificación: 1234")
                .fechaEnvio(LocalDateTime.now())
                .build();
    }

    @Test
    void testFindAll_DebeRetornar200() throws Exception {
        when(notificacionService.listAll()).thenReturn(Arrays.asList(notificacionMock));
        when(notificacionModelAssembler.toModel(any(Notificacion.class)))
                .thenReturn(EntityModel.of(notificacionMock));

        mockMvc.perform(get("/api/notificaciones/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.notificacionList[0].tipo").value("SMS"));
    }

    @Test
    void testFindAll_DebeRetornar204() throws Exception {
        when(notificacionService.listAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/notificaciones/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreate_DebeRetornar201() throws Exception {
        NotificacionRequest request = new NotificacionRequest();
        request.setIdUsuario(10L);
        request.setTipo("SMS");
        request.setMensaje("Código de verificación: 1234");
        request.setFechaEnvio(LocalDateTime.now());

        when(notificacionService.save(any(NotificacionRequest.class))).thenReturn(notificacionMock);

        EntityModel<Notificacion> entityModel = EntityModel.of(notificacionMock, Link.of("/api/notificaciones/1", "self"));
        when(notificacionModelAssembler.toModel(any(Notificacion.class))).thenReturn(entityModel);

        mockMvc.perform(post("/api/notificaciones/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        // Se utiliza la serialización predeterminada de Jackson para LocalDateTime
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("SMS"));
    }

    @Test
    void testFindById_DebeRetornar200() throws Exception {
        when(notificacionService.findById(1L)).thenReturn(notificacionMock);
        when(notificacionModelAssembler.toModel(any(Notificacion.class)))
                .thenReturn(EntityModel.of(notificacionMock));

        mockMvc.perform(get("/api/notificaciones/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(10L));
    }

    @Test
    void testFindByUsuario_DebeRetornar200() throws Exception {
        when(notificacionService.findByUsuario(10L)).thenReturn(Arrays.asList(notificacionMock));
        when(notificacionModelAssembler.toModel(any(Notificacion.class)))
                .thenReturn(EntityModel.of(notificacionMock));

        mockMvc.perform(get("/api/notificaciones/user/10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.notificacionList[0].tipo").value("SMS"));
    }

    @Test
    void testDeleteById_DebeRetornar204() throws Exception {
        doNothing().when(notificacionService).deleteById(1L);

        mockMvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isNoContent());

        verify(notificacionService, times(1)).deleteById(1L);
    }
}