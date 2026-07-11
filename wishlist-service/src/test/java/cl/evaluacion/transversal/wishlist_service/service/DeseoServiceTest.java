package cl.evaluacion.transversal.wishlist_service.service;

import cl.evaluacion.transversal.wishlist_service.dto.DeseoRequest;
import cl.evaluacion.transversal.wishlist_service.exception.WishlistNoExisteException;
import cl.evaluacion.transversal.wishlist_service.model.Deseo;
import cl.evaluacion.transversal.wishlist_service.repository.DeseoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeseoServiceTest {

    @Mock
    private DeseoRepository deseoRepository;

    @InjectMocks
    private DeseoService deseoService;

    private Deseo deseoMock;
    private DeseoRequest deseoRequestMock;

    @BeforeEach
    void setUp() {
        deseoMock = Deseo.builder()
                .id(1L)
                .idUsuario(15L)
                .idProducto(100L)
                .fechaAgregado(new Date())
                .build();

        deseoRequestMock = new DeseoRequest();
        deseoRequestMock.setIdUsuario(15L);
        deseoRequestMock.setIdProducto(100L);
        deseoRequestMock.setFechaAgregado(new Date());
    }

    @Test
    void testListByUsuario_Exito() {
        when(deseoRepository.findByIdUsuario(15L)).thenReturn(Optional.of(deseoMock));

        Deseo resultado = deseoService.listByUsuario(15L);

        assertNotNull(resultado);
        assertEquals(15L, resultado.getIdUsuario());
        assertEquals(100L, resultado.getIdProducto());
    }

    @Test
    void testListByUsuario_Fallo_LanzaExcepcion() {
        when(deseoRepository.findByIdUsuario(99L)).thenReturn(Optional.empty());

        assertThrows(WishlistNoExisteException.class, () -> deseoService.listByUsuario(99L));
    }

    @Test
    void testSave_Exito() {
        // Tu lógica exige que el idUsuario ya exista para poder guardar
        when(deseoRepository.findByIdUsuario(15L)).thenReturn(Optional.of(deseoMock));
        when(deseoRepository.save(any(Deseo.class))).thenAnswer(i -> {
            Deseo d = i.getArgument(0);
            d.setId(2L);
            return d;
        });

        Deseo resultado = deseoService.save(deseoRequestMock);

        assertNotNull(resultado.getId());
        assertEquals(100L, resultado.getIdProducto());
        verify(deseoRepository, times(1)).save(any(Deseo.class));
    }

    @Test
    void testSave_Fallo_LanzaExcepcionSiUsuarioNoEncontrado() {
        when(deseoRepository.findByIdUsuario(15L)).thenReturn(Optional.empty());

        assertThrows(WishlistNoExisteException.class, () -> deseoService.save(deseoRequestMock));
        verify(deseoRepository, never()).save(any(Deseo.class));
    }

    @Test
    void testDeleteByUsuario_Exito() {
        when(deseoRepository.findByIdUsuario(15L)).thenReturn(Optional.of(deseoMock));
        doNothing().when(deseoRepository).deleteByIdUsuario(15L);

        assertDoesNotThrow(() -> deseoService.deleteByUsuario(15L));
        verify(deseoRepository, times(1)).deleteByIdUsuario(15L);
    }

    @Test
    void testDeleteByUsuario_Fallo_LanzaExcepcion() {
        when(deseoRepository.findByIdUsuario(99L)).thenReturn(Optional.empty());

        assertThrows(WishlistNoExisteException.class, () -> deseoService.deleteByUsuario(99L));
        verify(deseoRepository, never()).deleteByIdUsuario(anyLong());
    }
}