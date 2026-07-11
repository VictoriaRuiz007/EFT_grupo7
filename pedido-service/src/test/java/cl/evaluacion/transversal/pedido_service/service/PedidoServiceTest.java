package cl.evaluacion.transversal.pedido_service.service;

import cl.evaluacion.transversal.pedido_service.dto.PedidoRequest;
import cl.evaluacion.transversal.pedido_service.exception.PedidoNoEncontradoException;
import cl.evaluacion.transversal.pedido_service.model.Pedido;
import cl.evaluacion.transversal.pedido_service.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedidoMock;
    private PedidoRequest pedidoRequestMock;

    @BeforeEach
    void setUp() {
        pedidoMock = Pedido.builder()
                .id(1L)
                .idUsuario(15L)
                .total(150000.0)
                .codigoPromocion("DESC10")
                .detalleProductos("Producto A x2, Producto B x1")
                .fechaPedido(new Date())
                .build();

        pedidoRequestMock = new PedidoRequest();
        pedidoRequestMock.setIdUsuario(15L);
        pedidoRequestMock.setTotal(150000.0);
        pedidoRequestMock.setCodigoPromocion("DESC10");
        pedidoRequestMock.setDetalleProductos("Producto A x2, Producto B x1");
        pedidoRequestMock.setFechaPedido(new Date());
    }

    @Test
    void testCreate_DebeCrearYRetornarPedido() {
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> {
            Pedido p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        Pedido resultado = pedidoService.create(pedidoRequestMock);

        assertNotNull(resultado.getId());
        assertEquals(150000.0, resultado.getTotal());
        assertEquals("DESC10", resultado.getCodigoPromocion());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testFindById_Exito() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        Pedido resultado = pedidoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testFindById_Fallo_LanzaExcepcion() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PedidoNoEncontradoException.class, () -> pedidoService.findById(99L));
    }

    @Test
    void testFindAll_DebeRetornarLista() {
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedidoMock));

        List<Pedido> resultado = pedidoService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testDeleteById_Exito() {
        when(pedidoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pedidoRepository).deleteById(1L);

        assertDoesNotThrow(() -> pedidoService.deleteById(1L));
        verify(pedidoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_Fallo_LanzaExcepcion() {
        when(pedidoRepository.existsById(99L)).thenReturn(false);

        assertThrows(PedidoNoEncontradoException.class, () -> pedidoService.deleteById(99L));
        verify(pedidoRepository, never()).deleteById(anyLong());
    }
}