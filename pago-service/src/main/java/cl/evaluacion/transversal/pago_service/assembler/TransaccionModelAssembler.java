package cl.evaluacion.transversal.pago_service.assembler;

import cl.evaluacion.transversal.pago_service.controller.TransaccionController;
import cl.evaluacion.transversal.pago_service.model.Transaccion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransaccionModelAssembler implements RepresentationModelAssembler<Transaccion, EntityModel<Transaccion>> {

    @Override
    public EntityModel<Transaccion> toModel(Transaccion transaccion) {
        return EntityModel.of(transaccion,
                linkTo(methodOn(TransaccionController.class).findById(transaccion.getId())).withSelfRel(),
                linkTo(methodOn(TransaccionController.class).findByIdPedido(transaccion.getIdPedido())).withRel("por-pedidos"),
                linkTo(methodOn(TransaccionController.class).findAll()).withRel("transacciones")
                );
    }
}
