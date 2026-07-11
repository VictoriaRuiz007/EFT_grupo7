package cl.evaluacion.transversal.notificacion_service.repository;

import cl.evaluacion.transversal.notificacion_service.model.Notificacion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {
    List<Notificacion> findByIdUsuario(Long idUsuario);
}
