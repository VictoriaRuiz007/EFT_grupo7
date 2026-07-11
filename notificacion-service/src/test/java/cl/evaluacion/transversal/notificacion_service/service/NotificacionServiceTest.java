package cl.evaluacion.transversal.notificacion_service.service;

import cl.evaluacion.transversal.notificacion_service.dto.NotificacionRequest;
import cl.evaluacion.transversal.notificacion_service.exception.NotificacionNoEncontradaException;
import cl.evaluacion.transversal.notificacion_service.exception.UsuarioNoEncontradoException;
import cl.evaluacion.transversal.notificacion_service.model.Notificacion;
import cl.evaluacion.transversal.notificacion_service.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacionMock;
    private NotificacionRequest notificacionRequestMock;

    @BeforeEach
    void setUp() {
        notificacionMock = Notificacion.builder()
                .id(1L)
                .idUsuario(10L)
                .tipo("EMAIL")
                .mensaje("Tu pedido ha sido enviado")
                .fechaEnvio(LocalDateTime.now())
                .build();

        notificacionRequestMock = new NotificacionRequest();
        notificacionRequestMock.setIdUsuario(10L);
        notificacionRequestMock.setTipo("EMAIL");
        notificacionRequestMock.setMensaje("Tu pedido ha sido enviado");
        notificacionRequestMock.setFechaEnvio(LocalDateTime.now());
    }

    @Test
    void testListAll_DebeRetornarLista() {
        when(notificacionRepository.findAll()).thenReturn(Arrays.asList(notificacionMock));

        List<Notificacion> resultado = notificacionService.listAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("EMAIL", resultado.get(0).getTipo());
    }

    @Test
    void testSave_DebeGuardarYRetornarNotificacion() {
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(i -> {
            Notificacion n = i.getArgument(0);
            n.setId(2L);
            return n;
        });

        Notificacion resultado = notificacionService.save(notificacionRequestMock);

        assertNotNull(resultado.getId());
        assertEquals("Tu pedido ha sido enviado", resultado.getMensaje());
        verify(notificacionRepository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void testFindById_Exito() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionMock));

        Notificacion resultado = notificacionService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testFindById_Fallo_DebeLanzarExcepcion() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotificacionNoEncontradaException.class, () -> notificacionService.findById(99L));
    }

    @Test
    void testDeleteById_Exito() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(notificacionRepository).deleteById(1L);

        assertDoesNotThrow(() -> notificacionService.deleteById(1L));
        verify(notificacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_Fallo_DebeLanzarExcepcion() {
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotificacionNoEncontradaException.class, () -> notificacionService.deleteById(99L));
        verify(notificacionRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindByUsuario_Exito() {
        when(notificacionRepository.findByIdUsuario(10L)).thenReturn(Arrays.asList(notificacionMock));

        List<Notificacion> resultado = notificacionService.findByUsuario(10L);

        assertFalse(resultado.isEmpty());
        assertEquals(10L, resultado.get(0).getIdUsuario());
    }

    @Test
    void testFindByUsuario_Fallo_DebeLanzarExcepcion() {
        when(notificacionRepository.findByIdUsuario(99L)).thenReturn(Collections.emptyList());

        assertThrows(UsuarioNoEncontradoException.class, () -> notificacionService.findByUsuario(99L));
    }
}