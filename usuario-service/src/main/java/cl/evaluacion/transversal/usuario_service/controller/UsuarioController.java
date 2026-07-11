package cl.evaluacion.transversal.usuario_service.controller;

import cl.evaluacion.transversal.usuario_service.assembler.UsuarioModelAsssembler;
import cl.evaluacion.transversal.usuario_service.dto.UsuarioRequest;
import cl.evaluacion.transversal.usuario_service.dto.UsuarioResponse;
import cl.evaluacion.transversal.usuario_service.model.Usuario;
import cl.evaluacion.transversal.usuario_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name="Usuarios", description="Gestion de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAsssembler  usuarioModelAssembler;

    @GetMapping("/")
    @Operation(summary="Ver todos los usuarios del sistema")
    @ApiResponse(responseCode = "200", description = "Usuarios encontrados")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> findAll() {
        List<Usuario> usuarios =  usuarioService.findAll();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Usuario>> models = usuarios.stream()
                .map(usuarioModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(UsuarioController.class).findAll()).withSelfRel()
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary="Buscar usuario por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Usuario>> findById(@PathVariable Long id) {
        Usuario u = usuarioService.findById(id);
        return ResponseEntity.ok(usuarioModelAssembler.toModel(u));
    }

    @PostMapping("/crear")
    @Operation(summary="Registrar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Usuario creado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Usuario>> create(@Valid @RequestBody UsuarioRequest ur) {
        Usuario u = usuarioService.save(ur);
        EntityModel<Usuario> model = usuarioModelAssembler.toModel(u);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar un usuario por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/correo/{correo}")
    @Operation(summary="Buscar usuario por su correo unico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description =  "Usuario no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Usuario>> findByCorreo(@PathVariable String correo) {
        Usuario u = usuarioService.findByCorreo(correo);
        return ResponseEntity.ok(usuarioModelAssembler.toModel(u));
    }
}