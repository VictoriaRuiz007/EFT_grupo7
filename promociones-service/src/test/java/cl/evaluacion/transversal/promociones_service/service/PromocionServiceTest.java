package cl.evaluacion.transversal.promociones_service.service;

import cl.evaluacion.transversal.promociones_service.dto.PromocionRequest;
import cl.evaluacion.transversal.promociones_service.exception.PromocionExisteException;
import cl.evaluacion.transversal.promociones_service.exception.PromocionNoEncontradaException;
import cl.evaluacion.transversal.promociones_service.model.Promocion;
import cl.evaluacion.transversal.promociones_service.repository.PromocionRepository;
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
class PromocionServiceTest {

    @Mock
    private PromocionRepository promocionRepository;

    @InjectMocks
    private PromocionService promocionService;

    private Promocion promocionMock;
    private PromocionRequest promocionRequestMock;

    @BeforeEach
    void setUp() {
        promocionMock = Promocion.builder()
                .id(1L)
                .codigo("VERANO20")
                .porcentajeDescuento(20.0)
                .fechaExpiracion(new Date())
                .activo(true)
                .build();

        promocionRequestMock = new PromocionRequest();
        promocionRequestMock.setCodigo("VERANO20");
        promocionRequestMock.setPorcentajeDescuento(20.0);
        promocionRequestMock.setFechaExpiracion(new Date());
    }

    @Test
    void testFindAll_DebeRetornarLista() {
        when(promocionRepository.findAll()).thenReturn(Arrays.asList(promocionMock));

        List<Promocion> resultado = promocionService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testSave_Exito() {
        // Simular que el código no existe
        when(promocionRepository.findByCodigo("VERANO20")).thenReturn(Optional.empty());
        when(promocionRepository.save(any(Promocion.class))).thenAnswer(i -> {
            Promocion p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        Promocion resultado = promocionService.save(promocionRequestMock);

        assertNotNull(resultado.getId());
        assertTrue(resultado.isActivo());
        assertEquals(20.0, resultado.getPorcentajeDescuento());
        verify(promocionRepository, times(1)).save(any(Promocion.class));
    }

    @Test
    void testSave_Fallo_LanzaExcepcionSiYaExiste() {
        // Simular que el código YA existe
        when(promocionRepository.findByCodigo("VERANO20")).thenReturn(Optional.of(promocionMock));

        assertThrows(PromocionExisteException.class, () -> promocionService.save(promocionRequestMock));
        verify(promocionRepository, never()).save(any(Promocion.class));
    }

    @Test
    void testFindById_Exito() {
        when(promocionRepository.findById(1L)).thenReturn(Optional.of(promocionMock));

        Promocion resultado = promocionService.findById(1L);

        assertNotNull(resultado);
        assertEquals("VERANO20", resultado.getCodigo());
    }

    @Test
    void testFindById_Fallo_LanzaExcepcion() {
        when(promocionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PromocionNoEncontradaException.class, () -> promocionService.findById(99L));
    }

    @Test
    void testFindByCodigo_Exito() {
        when(promocionRepository.findByCodigo("VERANO20")).thenReturn(Optional.of(promocionMock));

        Promocion resultado = promocionService.findByCodigo("VERANO20");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testFindByCodigo_Fallo_LanzaExcepcion() {
        when(promocionRepository.findByCodigo("INEXISTENTE")).thenReturn(Optional.empty());

        assertThrows(PromocionNoEncontradaException.class, () -> promocionService.findByCodigo("INEXISTENTE"));
    }

    @Test
    void testDeleteById_Exito() {
        when(promocionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(promocionRepository).deleteById(1L);

        assertDoesNotThrow(() -> promocionService.deleteById(1L));
        verify(promocionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_Fallo_LanzaExcepcion() {
        when(promocionRepository.existsById(99L)).thenReturn(false);

        assertThrows(PromocionNoEncontradaException.class, () -> promocionService.deleteById(99L));
        verify(promocionRepository, never()).deleteById(anyLong());
    }
}