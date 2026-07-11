package cl.evaluacion.transversal.faq_service.repository;

import cl.evaluacion.transversal.faq_service.model.FAQ;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
