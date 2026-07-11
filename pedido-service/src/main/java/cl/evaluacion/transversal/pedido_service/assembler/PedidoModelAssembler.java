package cl.evaluacion.transversal.pedido_service.assembler;

import cl.evaluacion.transversal.pedido_service.controller.PedidoController;
import cl.evaluacion.transversal.pedido_service.model.Pedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).findById(pedido.getId())).withSelfRel(),
                linkTo(methodOn(PedidoController.class).findAll()).withRel("pedidos")
                );
    }
}
