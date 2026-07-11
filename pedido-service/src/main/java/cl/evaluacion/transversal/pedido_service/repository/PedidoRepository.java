package cl.evaluacion.transversal.pedido_service.repository;

import cl.evaluacion.transversal.pedido_service.model.Pedido;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface PedidoRepository extends JpaRepository<Pedido,Long> {
}
