package cl.evaluacion.transversal.catalogo_service.repository;

import cl.evaluacion.transversal.catalogo_service.model.Videojuego;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional

public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {
}
