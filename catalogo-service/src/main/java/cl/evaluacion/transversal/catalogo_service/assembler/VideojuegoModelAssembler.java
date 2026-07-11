package cl.evaluacion.transversal.catalogo_service.assembler;

import cl.evaluacion.transversal.catalogo_service.controller.VideojuegoController;
import cl.evaluacion.transversal.catalogo_service.model.Videojuego;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VideojuegoModelAssembler implements RepresentationModelAssembler<Videojuego, EntityModel<Videojuego>> {

    @Override
    public EntityModel<Videojuego> toModel(Videojuego videojuego) {
        return EntityModel.of(videojuego,
                linkTo(methodOn(VideojuegoController.class).buscarPorId(videojuego.getId())).withSelfRel(),
                linkTo(methodOn(VideojuegoController.class).listarTodos()).withRel("productos")
        );
    }
}
