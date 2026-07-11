package cl.evaluacion.transversal.promociones_service.repository;

import cl.evaluacion.transversal.promociones_service.model.Promocion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface PromocionRepository extends JpaRepository<Promocion,Long> {
    Optional<Promocion> findByCodigo(String codigo);
}
