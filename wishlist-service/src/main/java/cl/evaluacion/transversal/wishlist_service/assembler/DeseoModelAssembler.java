package cl.evaluacion.transversal.wishlist_service.assembler;

import cl.evaluacion.transversal.wishlist_service.controller.DeseoController;
import cl.evaluacion.transversal.wishlist_service.model.Deseo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeseoModelAssembler implements RepresentationModelAssembler<Deseo, EntityModel<Deseo>> {

    @Override
    public EntityModel<Deseo> toModel(Deseo deseo) {
        return EntityModel.of(deseo,
                linkTo(methodOn(DeseoController.class).findByIdUsuario(deseo.getIdUsuario())).withSelfRel()
                );
    }
}
