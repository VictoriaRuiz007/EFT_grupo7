package cl.evaluacion.transversal.promociones_service.assembler;

import cl.evaluacion.transversal.promociones_service.controller.PromocionController;
import cl.evaluacion.transversal.promociones_service.model.Promocion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PromocionModelAssembler implements RepresentationModelAssembler<Promocion, EntityModel<Promocion>> {

    @Override
    public EntityModel<Promocion> toModel(Promocion promocion) {
        return EntityModel.of(promocion,
                linkTo(methodOn(PromocionController.class).findById(promocion.getId())).withSelfRel(),
                linkTo(methodOn(PromocionController.class).findByCodigo(promocion.getCodigo())).withRel("por-codigo"),
                linkTo(methodOn(PromocionController.class).findAll()).withRel("promociones")
                );
    }

}
