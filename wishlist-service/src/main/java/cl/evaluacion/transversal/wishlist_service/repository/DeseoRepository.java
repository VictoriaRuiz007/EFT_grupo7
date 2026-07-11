package cl.evaluacion.transversal.wishlist_service.repository;

import cl.evaluacion.transversal.wishlist_service.model.Deseo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface DeseoRepository extends JpaRepository<Deseo,Long> {
    Optional<Deseo> findByIdUsuario(Long idUsuario);
    void deleteByIdUsuario(Long idUsuario);
}
