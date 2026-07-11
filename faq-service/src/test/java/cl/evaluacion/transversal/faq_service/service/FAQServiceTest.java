package cl.evaluacion.transversal.faq_service.service;

import cl.evaluacion.transversal.faq_service.dto.FAQRequest;
import cl.evaluacion.transversal.faq_service.exception.FAQNoEncontradaException;
import cl.evaluacion.transversal.faq_service.model.FAQ;
import cl.evaluacion.transversal.faq_service.repository.FAQRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FAQServiceTest {

    @Mock
    private FAQRepository faqRepository;

    @InjectMocks
    private FAQService faqService;

    private FAQ faqMock;
    private FAQRequest faqRequestMock;

    @BeforeEach
    void setUp() {
        faqMock = FAQ.builder()
                .id(1L)
                .pregunta("¿Cómo recupero mi contraseña?")
                .respuesta("Debes ir a la sección de 'Olvidé mi contraseña'.")
                .categoria("Cuenta")
                .build();

        // Si tu FAQRequest usa setters tradicionales:
        faqRequestMock = new FAQRequest();
        faqRequestMock.setPregunta("¿Hacen envíos internacionales?");
        faqRequestMock.setRespuesta("No, por el momento solo envíos nacionales.");
        faqRequestMock.setCategoria("Envíos");
    }

    @Test
    void testFindAll_DebeRetornarListaDeFAQs() {
        when(faqRepository.findAll()).thenReturn(Arrays.asList(faqMock));

        List<FAQ> resultado = faqService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("¿Cómo recupero mi contraseña?", resultado.get(0).getPregunta());
        verify(faqRepository, times(1)).findAll();
    }

    @Test
    void testCreate_DebeGuardarYRetornarFAQ() {
        when(faqRepository.save(any(FAQ.class))).thenAnswer(invocation -> {
            FAQ guardado = invocation.getArgument(0);
            guardado.setId(2L);
            return guardado;
        });

        FAQ resultado = faqService.create(faqRequestMock);

        assertNotNull(resultado.getId());
        assertEquals("¿Hacen envíos internacionales?", resultado.getPregunta());
        assertEquals("Envíos", resultado.getCategoria());
        verify(faqRepository, times(1)).save(any(FAQ.class));
    }

    @Test
    void testDelete_Exito_DebeEliminarFAQ() {
        when(faqRepository.existsById(1L)).thenReturn(true);
        doNothing().when(faqRepository).deleteById(1L);

        assertDoesNotThrow(() -> faqService.delete(1L));
        verify(faqRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_Fallo_DebeLanzarExcepcionSiNoExiste() {
        when(faqRepository.existsById(99L)).thenReturn(false);

        assertThrows(FAQNoEncontradaException.class, () -> faqService.delete(99L));
        verify(faqRepository, never()).deleteById(anyLong());
    }
}