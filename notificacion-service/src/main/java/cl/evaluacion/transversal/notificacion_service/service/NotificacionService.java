package cl.evaluacion.transversal.notificacion_service.service;

import cl.evaluacion.transversal.notificacion_service.dto.NotificacionRequest;
import cl.evaluacion.transversal.notificacion_service.exception.NotificacionNoEncontradaException;
import cl.evaluacion.transversal.notificacion_service.exception.UsuarioNoEncontradoException;
import cl.evaluacion.transversal.notificacion_service.model.Notificacion;
import cl.evaluacion.transversal.notificacion_service.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> listAll(){
        log.info("Listando todos los notificaciones: ");
        return notificacionRepository.findAll();
    }

    public Notificacion save(NotificacionRequest nr){
        log.info("Guardando notificacion : {}", nr);

        Notificacion n = new Notificacion();
        n.setMensaje(nr.getMensaje());
        n.setTipo(nr.getTipo());
        n.setIdUsuario(nr.getIdUsuario());
        n.setFechaEnvio(nr.getFechaEnvio());
        return notificacionRepository.save(n);
    }

    public Notificacion findById(Long id){
        log.info("Buscando notificacion con id: {}", id);
        return notificacionRepository.findById(id)
                .orElseThrow(()-> new NotificacionNoEncontradaException("Notificacion no encontrada"));
    }

    public void deleteById(Long id){
        log.info("Eliminando notificacion por id : {}", id);
        if(!notificacionRepository.existsById(id)){
            throw new NotificacionNoEncontradaException("Notificacion no encontrada");
        }
        notificacionRepository.deleteById(id);
    }

    public List<Notificacion> findByUsuario(Long idUsuario){
        log.info("Buscando notificaciones por el usuario de id : {}", idUsuario);
        List<Notificacion> ns = notificacionRepository.findByIdUsuario(idUsuario);

        if(ns.isEmpty()){
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }
        return ns;
    }
}
