package cl.evaluacion.transversal.logistica_service.repository;

import cl.evaluacion.transversal.logistica_service.model.Seguimiento;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
    Optional<Seguimiento> findByCodigo(String codigo);
}
