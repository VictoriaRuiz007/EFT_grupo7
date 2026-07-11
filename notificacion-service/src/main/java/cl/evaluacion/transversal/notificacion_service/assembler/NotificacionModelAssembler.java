package cl.evaluacion.transversal.notificacion_service.assembler;

import cl.evaluacion.transversal.notificacion_service.controller.NotificacionController;
import cl.evaluacion.transversal.notificacion_service.model.Notificacion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class NotificacionModelAssembler implements RepresentationModelAssembler<Notificacion, EntityModel<Notificacion>> {

    @Override
    public EntityModel<Notificacion> toModel(Notificacion notificacion) {
        return EntityModel.of(notificacion,
                linkTo(methodOn(NotificacionController.class).findById(notificacion.getId())).withSelfRel(),
                linkTo(methodOn(NotificacionController.class).findByUsuario(notificacion.getIdUsuario())).withRel("por-usuario"),
                linkTo(methodOn(NotificacionController.class).findAll()).withRel("notificaciones")
                );
    }
}
