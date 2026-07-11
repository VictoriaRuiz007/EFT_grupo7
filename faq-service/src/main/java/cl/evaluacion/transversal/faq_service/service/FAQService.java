package cl.evaluacion.transversal.faq_service.service;

import cl.evaluacion.transversal.faq_service.dto.FAQRequest;
import cl.evaluacion.transversal.faq_service.exception.FAQNoEncontradaException;
import cl.evaluacion.transversal.faq_service.model.FAQ;
import cl.evaluacion.transversal.faq_service.repository.FAQRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FAQService {

    private static final Logger log = LoggerFactory.getLogger(FAQService.class);

    @Autowired
    private FAQRepository faqRepository;

    public List<FAQ> findAll() {
        log.info("Mostrando todas las FAQ:");
        return faqRepository.findAll();
    }

    public FAQ create(FAQRequest fr) {
        log.info("Creando nueva FAQ: {}", fr.getPregunta());
        FAQ f = new FAQ();
        f.setPregunta(fr.getPregunta());
        f.setRespuesta(fr.getRespuesta());
        f.setCategoria(fr.getCategoria());
        return faqRepository.save(f);
    }

    public void delete(Long id) {
        log.info("Eliminando FAQ con id: {}", id);
        if(!faqRepository.existsById(id)){
            throw new FAQNoEncontradaException("No existe el FAQ con el id: " + id);
        }
        faqRepository.deleteById(id);
    }
}
