package cl.evaluacion.transversal.usuario_service.service;

import cl.evaluacion.transversal.usuario_service.dto.UsuarioRequest;
import cl.evaluacion.transversal.usuario_service.exception.CorreoNoEncontradoException;
import cl.evaluacion.transversal.usuario_service.exception.UsuarioExisteException;
import cl.evaluacion.transversal.usuario_service.exception.UsuarioNoEncontradoException;
import cl.evaluacion.transversal.usuario_service.model.Usuario;
import cl.evaluacion.transversal.usuario_service.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;
    private UsuarioRequest usuarioRequestMock;

    @BeforeEach
    void setUp() {
        usuarioMock = Usuario.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan.perez@email.com")
                .contrasenia("password123")
                .rol("CLIENTE")
                .build();

        usuarioRequestMock = new UsuarioRequest();
        usuarioRequestMock.setNombre("Juan");
        usuarioRequestMock.setApellido("Pérez");
        usuarioRequestMock.setCorreo("juan.perez@email.com");
        usuarioRequestMock.setContrasenia("password123");
    }

    @Test
    void testFindAll_DebeRetornarLista() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioMock));

        List<Usuario> resultado = usuarioService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testFindById_Exito() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        Usuario resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void testFindById_Fallo_LanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.findById(99L));
    }

    @Test
    void testFindByCorreo_Exito() {
        when(usuarioRepository.findByCorreo("juan.perez@email.com")).thenReturn(Optional.of(usuarioMock));

        Usuario resultado = usuarioService.findByCorreo("juan.perez@email.com");

        assertNotNull(resultado);
        assertEquals("juan.perez@email.com", resultado.getCorreo());
    }

    @Test
    void testFindByCorreo_Fallo_LanzaExcepcion() {
        when(usuarioRepository.findByCorreo("no.existe@email.com")).thenReturn(Optional.empty());

        assertThrows(CorreoNoEncontradoException.class, () -> usuarioService.findByCorreo("no.existe@email.com"));
    }

    @Test
    void testSave_Exito() {
        when(usuarioRepository.findByCorreo("juan.perez@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        Usuario resultado = usuarioService.save(usuarioRequestMock);

        assertNotNull(resultado.getId());
        assertEquals("CLIENTE", resultado.getRol()); // Validar que se asigne CLIENTE por defecto
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testSave_Fallo_LanzaExcepcionSiCorreoExiste() {
        when(usuarioRepository.findByCorreo("juan.perez@email.com")).thenReturn(Optional.of(usuarioMock));

        assertThrows(UsuarioExisteException.class, () -> usuarioService.save(usuarioRequestMock));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testDeleteById_Exito() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> usuarioService.deleteById(1L));
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_Fallo_LanzaExcepcion() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.deleteById(99L));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}
