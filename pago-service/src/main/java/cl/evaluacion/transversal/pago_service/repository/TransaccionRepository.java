package cl.evaluacion.transversal.pago_service.repository;

import cl.evaluacion.transversal.pago_service.model.Transaccion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface TransaccionRepository extends JpaRepository<Transaccion,Long> {
    Optional<Transaccion> findByIdPedido(Long idPedido);
}
