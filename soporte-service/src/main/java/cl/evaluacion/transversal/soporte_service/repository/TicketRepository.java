package cl.evaluacion.transversal.soporte_service.repository;

import cl.evaluacion.transversal.soporte_service.model.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByEstado(String estado);
}
