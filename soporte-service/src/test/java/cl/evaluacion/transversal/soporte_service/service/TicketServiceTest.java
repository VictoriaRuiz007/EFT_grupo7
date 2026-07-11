package cl.evaluacion.transversal.soporte_service.service;

import cl.evaluacion.transversal.soporte_service.dto.TicketRequest;
import cl.evaluacion.transversal.soporte_service.exception.EstadoNoEncontradoException;
import cl.evaluacion.transversal.soporte_service.exception.TicketNoEncontradoException;
import cl.evaluacion.transversal.soporte_service.model.Ticket;
import cl.evaluacion.transversal.soporte_service.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticketMock;
    private TicketRequest ticketRequestMock;

    @BeforeEach
    void setUp() {
        ticketMock = Ticket.builder()
                .id(1L)
                .idUsuario(10L)
                .asunto("Problema con pago")
                .descripcion("Se cobró dos veces mi pedido.")
                .estado("ABIERTO")
                .fechaCreacion(new Date())
                .build();

        ticketRequestMock = new TicketRequest();
        ticketRequestMock.setIdUsuario(10L);
        ticketRequestMock.setAsunto("Problema con pago");
        ticketRequestMock.setDescripcion("Se cobró dos veces mi pedido.");
        ticketRequestMock.setEstado("ABIERTO");
        ticketRequestMock.setFechaCreacion(new Date());
    }

    @Test
    void testFindAll_DebeRetornarLista() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticketMock));

        List<Ticket> resultado = ticketService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testFindById_Exito() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketMock));

        Ticket resultado = ticketService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Problema con pago", resultado.getAsunto());
    }

    @Test
    void testFindById_Fallo_LanzaExcepcion() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TicketNoEncontradoException.class, () -> ticketService.findById(99L));
    }

    @Test
    void testFindByEstado_Exito() {
        when(ticketRepository.findByEstado("ABIERTO")).thenReturn(Arrays.asList(ticketMock));

        List<Ticket> resultado = ticketService.findByEstado("ABIERTO");

        assertFalse(resultado.isEmpty());
        assertEquals("ABIERTO", resultado.get(0).getEstado());
    }

    @Test
    void testFindByEstado_Fallo_LanzaExcepcion() {
        when(ticketRepository.findByEstado("CERRADO")).thenReturn(Collections.emptyList());

        assertThrows(EstadoNoEncontradoException.class, () -> ticketService.findByEstado("CERRADO"));
    }

    @Test
    void testSave_DebeGuardarYRetornarTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> {
            Ticket t = i.getArgument(0);
            t.setId(1L);
            return t;
        });

        Ticket resultado = ticketService.save(ticketRequestMock);

        assertNotNull(resultado.getId());
        assertEquals("ABIERTO", resultado.getEstado());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testDeleteById_Exito() {
        when(ticketRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ticketRepository).deleteById(1L);

        assertDoesNotThrow(() -> ticketService.deleteById(1L));
        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_Fallo_LanzaExcepcion() {
        when(ticketRepository.existsById(99L)).thenReturn(false);

        assertThrows(TicketNoEncontradoException.class, () -> ticketService.deleteById(99L));
        verify(ticketRepository, never()).deleteById(anyLong());
    }
}