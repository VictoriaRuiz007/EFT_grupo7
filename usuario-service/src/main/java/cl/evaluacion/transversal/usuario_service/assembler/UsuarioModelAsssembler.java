package cl.evaluacion.transversal.usuario_service.assembler;

import cl.evaluacion.transversal.usuario_service.controller.UsuarioController;
import cl.evaluacion.transversal.usuario_service.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAsssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).findById(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).findByCorreo(usuario.getCorreo())).withRel("por-correo"),
                linkTo(methodOn(UsuarioController.class).findAll()).withRel("usuarios")
                );
    }
}
