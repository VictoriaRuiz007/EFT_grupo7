package cl.evaluacion.transversal.pago_service.service;

import cl.evaluacion.transversal.pago_service.dto.TransaccionRequest;
import cl.evaluacion.transversal.pago_service.exception.PedidoNoEncontradoException;
import cl.evaluacion.transversal.pago_service.exception.TransaccionNoEncontradaException;
import cl.evaluacion.transversal.pago_service.model.Transaccion;
import cl.evaluacion.transversal.pago_service.repository.TransaccionRepository;
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
class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    private Transaccion transaccionMock;
    private TransaccionRequest transaccionRequestMock;

    @BeforeEach
    void setUp() {
        transaccionMock = Transaccion.builder()
                .id(1L)
                .idPedido(100L)
                .metodoPago("Tarjeta de Crédito")
                .monto(45000.0)
                .estado("Aprobado")
                .fecha(LocalDateTime.now())
                .build();

        transaccionRequestMock = new TransaccionRequest();
        transaccionRequestMock.setIdPedido(100L);
        transaccionRequestMock.setMetodoPago("Tarjeta de Crédito");
        transaccionRequestMock.setMonto(45000.0);
        transaccionRequestMock.setEstado("Aprobado");
        transaccionRequestMock.setFecha(LocalDateTime.now());
    }

    @Test
    void testSave_DebeGuardarYRetornarTransaccion() {
        when(transaccionRepository.save(any(Transaccion.class))).thenAnswer(i -> {
            Transaccion t = i.getArgument(0);
            t.setId(1L);
            return t;
        });

        Transaccion resultado = transaccionService.save(transaccionRequestMock);

        assertNotNull(resultado.getId());
        assertEquals(45000.0, resultado.getMonto());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void testFindById_Exito() {
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccionMock));

        Transaccion resultado = transaccionService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testFindById_Fallo_LanzaExcepcion() {
        when(transaccionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TransaccionNoEncontradaException.class, () -> transaccionService.findById(99L));
    }

    @Test
    void testFindAll_DebeRetornarLista() {
        when(transaccionRepository.findAll()).thenReturn(Arrays.asList(transaccionMock));

        List<Transaccion> resultado = transaccionService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testDeleteById_Exito() {
        when(transaccionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(transaccionRepository).deleteById(1L);

        assertDoesNotThrow(() -> transaccionService.deleteById(1L));
        verify(transaccionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_Fallo_LanzaExcepcion() {
        when(transaccionRepository.existsById(99L)).thenReturn(false);

        assertThrows(TransaccionNoEncontradaException.class, () -> transaccionService.deleteById(99L));
        verify(transaccionRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindByIdPedido_Exito() {
        when(transaccionRepository.findByIdPedido(100L)).thenReturn(Optional.of(transaccionMock));

        Transaccion resultado = transaccionService.findByIdPedido(100L);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getIdPedido());
    }

    @Test
    void testFindByIdPedido_Fallo_LanzaExcepcion() {
        when(transaccionRepository.findByIdPedido(999L)).thenReturn(Optional.empty());

        assertThrows(PedidoNoEncontradoException.class, () -> transaccionService.findByIdPedido(999L));
    }
}