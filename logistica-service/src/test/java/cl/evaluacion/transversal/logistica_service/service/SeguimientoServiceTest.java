package cl.evaluacion.transversal.logistica_service.service;

import cl.evaluacion.transversal.logistica_service.dto.LogisticaRequest;
import cl.evaluacion.transversal.logistica_service.exception.CodigoNoEncontradoException;
import cl.evaluacion.transversal.logistica_service.exception.SeguimientoExisteException;
import cl.evaluacion.transversal.logistica_service.exception.SeguimientoNoEncontradoException;
import cl.evaluacion.transversal.logistica_service.model.Seguimiento;
import cl.evaluacion.transversal.logistica_service.repository.SeguimientoRepository;
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
class SeguimientoServiceTest {

    @Mock
    private SeguimientoRepository seguimientoRepository;

    @InjectMocks
    private SeguimientoService seguimientoService;

    private Seguimiento seguimientoMock;
    private LogisticaRequest logisticaRequestMock;

    @BeforeEach
    void setUp() {
        seguimientoMock = Seguimiento.builder()
                .id(1L)
                .codigoSeguimiento("TRK-123")
                .empresaEnvio("Chilexpress")
                .estado("En ruta")
                .direccionDestino("Av. Siempreviva 742")
                .fechaEstimada(new Date())
                .idPedido(100L)
                .build();

        logisticaRequestMock = new LogisticaRequest();
        logisticaRequestMock.setCodigoSeguimiento("TRK-123");
        logisticaRequestMock.setEmpresaEnvio("Chilexpress");
        logisticaRequestMock.setEstado("En ruta");
        logisticaRequestMock.setDireccionDestino("Av. Siempreviva 742");
        logisticaRequestMock.setFechaEstimada(new Date());
    }

    @Test
    void testFindAll_DebeRetornarLista() {
        when(seguimientoRepository.findAll()).thenReturn(Arrays.asList(seguimientoMock));

        List<Seguimiento> resultado = seguimientoService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("TRK-123", resultado.get(0).getCodigoSeguimiento());
    }

    @Test
    void testFindByCodigo_Exito_DebeRetornarSeguimiento() {
        when(seguimientoRepository.findByCodigo("TRK-123")).thenReturn(Optional.of(seguimientoMock));

        Seguimiento resultado = seguimientoService.findByCodigo("TRK-123");

        assertNotNull(resultado);
        assertEquals("TRK-123", resultado.getCodigoSeguimiento());
    }

    @Test
    void testFindByCodigo_Fallo_DebeLanzarExcepcion() {
        when(seguimientoRepository.findByCodigo("FALSO")).thenReturn(Optional.empty());

        assertThrows(CodigoNoEncontradoException.class, () -> seguimientoService.findByCodigo("FALSO"));
    }

    @Test
    void testSave_Exito_DebeGuardar() {
        // Para que save sea exitoso, findAll debe retornar una lista que NO contenga el código "TRK-123"
        when(seguimientoRepository.findAll()).thenReturn(Collections.emptyList());
        when(seguimientoRepository.save(any(Seguimiento.class))).thenAnswer(i -> {
            Seguimiento s = i.getArgument(0);
            s.setId(2L);
            return s;
        });

        Seguimiento resultado = seguimientoService.save(logisticaRequestMock);

        assertNotNull(resultado.getId());
        assertEquals("TRK-123", resultado.getCodigoSeguimiento());
    }

    @Test
    void testSave_Fallo_DebeLanzarExcepcionSiExiste() {
        // Simulamos que ya existe un seguimiento con el mismo código en la base de datos
        when(seguimientoRepository.findAll()).thenReturn(Arrays.asList(seguimientoMock));

        assertThrows(SeguimientoExisteException.class, () -> seguimientoService.save(logisticaRequestMock));
        verify(seguimientoRepository, never()).save(any(Seguimiento.class));
    }

    @Test
    void testFindById_Exito() {
        when(seguimientoRepository.findById(1L)).thenReturn(Optional.of(seguimientoMock));
        Seguimiento resultado = seguimientoService.findById(1L);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testFindById_Fallo_LanzaExcepcion() {
        when(seguimientoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(SeguimientoNoEncontradoException.class, () -> seguimientoService.findById(99L));
    }

    @Test
    void testDeleteById_Exito() {
        when(seguimientoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(seguimientoRepository).deleteById(1L);

        assertDoesNotThrow(() -> seguimientoService.deleteById(1L));
        verify(seguimientoRepository, times(1)).deleteById(1L);
    }
}