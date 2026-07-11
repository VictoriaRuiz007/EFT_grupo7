package cl.evaluacion.transversal.catalogo_service.service;

import cl.evaluacion.transversal.catalogo_service.dto.VideojuegoRequest;
import cl.evaluacion.transversal.catalogo_service.exception.ProductoExistenteException;
import cl.evaluacion.transversal.catalogo_service.exception.ProductoNoEncontradoException;
import cl.evaluacion.transversal.catalogo_service.model.Videojuego;
import cl.evaluacion.transversal.catalogo_service.repository.VideojuegoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideojuegoServiceTest {

    @Mock
    private VideojuegoRepository videojuegoRepository;

    @InjectMocks
    private VideojuegoService videojuegoService;

    private Videojuego videojuegoMock;
    private VideojuegoRequest videojuegoRequestMock;

    @BeforeEach
    void setUp() {
        videojuegoMock = Videojuego.builder()
                .id(1L)
                .titulo("Super Smash Bros")
                .plataforma("Nintendo Switch")
                .stock(15)
                .categoria("Peleas")
                .precio(59.99)
                .build();

        // Asumiendo la estructura de tu DTO basada en tu servicio
        videojuegoRequestMock = new VideojuegoRequest();
        videojuegoRequestMock.setTitulo("Zelda Tears of the Kingdom");
        videojuegoRequestMock.setPlataforma("Nintendo Switch");
        videojuegoRequestMock.setStock(20);
        videojuegoRequestMock.setCategoria("Aventura");
        videojuegoRequestMock.setPrecio(69.99);
    }

    @Test
    void testListAll_DebeRetornarListaDeVideojuegos() {
        when(videojuegoRepository.findAll()).thenReturn(Arrays.asList(videojuegoMock));

        List<Videojuego> resultado = videojuegoService.listAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Super Smash Bros", resultado.get(0).getTitulo());
        verify(videojuegoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Exito_DebeRetornarVideojuego() {
        when(videojuegoRepository.findById(1L)).thenReturn(Optional.of(videojuegoMock));

        Videojuego resultado = videojuegoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Super Smash Bros", resultado.getTitulo());
    }

    @Test
    void testFindById_Fallo_DebeLanzarExcepcion() {
        when(videojuegoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class, () -> videojuegoService.findById(99L));
    }

    @Test
    void testSave_Exito_DebeGuardarVideojuego() {
        // Simulamos que no hay juegos con ese título
        when(videojuegoRepository.findAll()).thenReturn(Arrays.asList(videojuegoMock));
        when(videojuegoRepository.save(any(Videojuego.class))).thenAnswer(invocation -> {
            Videojuego guardado = invocation.getArgument(0);
            guardado.setId(2L);
            return guardado;
        });

        Videojuego resultado = videojuegoService.save(videojuegoRequestMock);

        assertNotNull(resultado.getId());
        assertEquals("Zelda Tears of the Kingdom", resultado.getTitulo());
        verify(videojuegoRepository, times(1)).save(any(Videojuego.class));
    }

    @Test
    void testSave_Fallo_DebeLanzarExcepcionSiExiste() {
        // Configuramos el request para que tenga el mismo título que el mock existente
        videojuegoRequestMock.setTitulo("Super Smash Bros");
        when(videojuegoRepository.findAll()).thenReturn(Arrays.asList(videojuegoMock));

        assertThrows(ProductoExistenteException.class, () -> videojuegoService.save(videojuegoRequestMock));
        verify(videojuegoRepository, never()).save(any(Videojuego.class));
    }

    @Test
    void testDelete_Exito_DebeEliminarVideojuego() {
        when(videojuegoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(videojuegoRepository).deleteById(1L);

        assertDoesNotThrow(() -> videojuegoService.delete(1L));
        verify(videojuegoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_Fallo_DebeLanzarExcepcionSiNoExiste() {
        when(videojuegoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductoNoEncontradoException.class, () -> videojuegoService.delete(99L));
        verify(videojuegoRepository, never()).deleteById(anyLong());
    }
}