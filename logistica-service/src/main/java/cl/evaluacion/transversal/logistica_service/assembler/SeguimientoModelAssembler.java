package cl.evaluacion.transversal.logistica_service.assembler;

import cl.evaluacion.transversal.logistica_service.controller.SeguimientoController;
import cl.evaluacion.transversal.logistica_service.model.Seguimiento;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SeguimientoModelAssembler implements RepresentationModelAssembler<Seguimiento, EntityModel<Seguimiento>> {

    @Override
    public EntityModel<Seguimiento> toModel(Seguimiento seguimiento) {
        return EntityModel.of(seguimiento,
                linkTo(methodOn(SeguimientoController.class).findById(seguimiento.getId())).withSelfRel(),
                linkTo(methodOn(SeguimientoController.class).findByCodigo(seguimiento.getCodigoSeguimiento())).withRel("por-codigo"),
                linkTo(methodOn(SeguimientoController.class).findAll()).withRel("seguimientos")
        );
    }
}
