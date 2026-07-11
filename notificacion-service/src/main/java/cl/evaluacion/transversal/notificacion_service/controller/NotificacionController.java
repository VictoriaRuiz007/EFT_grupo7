package cl.evaluacion.transversal.notificacion_service.controller;

import cl.evaluacion.transversal.notificacion_service.assembler.NotificacionModelAssembler;
import cl.evaluacion.transversal.notificacion_service.dto.NotificacionRequest;
import cl.evaluacion.transversal.notificacion_service.dto.NotificacionResponse;
import cl.evaluacion.transversal.notificacion_service.model.Notificacion;
import cl.evaluacion.transversal.notificacion_service.service.NotificacionService;
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
@RequestMapping("/api/notificaciones")
@Tag(name="Notificaciones", description="Acciones relacionadas al manejo de notificaciones del sistema")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private NotificacionModelAssembler notificacionModelAssembler;

    @GetMapping("/")
    @Operation(summary="Ver todas las notificaciones enviadas")
    @ApiResponse(responseCode = "200", description = "Notificaciones encontradas")
    public ResponseEntity<CollectionModel<EntityModel<Notificacion>>> findAll() {
        List<Notificacion> notificacions = notificacionService.listAll();
        if(notificacions.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Notificacion>> models = notificacions.stream()
                .map(notificacionModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(NotificacionController.class).findAll()).withSelfRel()
                ));
    }

    @PostMapping("/crear")
    @Operation(summary="Crear una notificación hacia un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Notificacion creada"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Notificacion>> create(@Valid @RequestBody NotificacionRequest nr) {
        Notificacion n = notificacionService.save(nr);
        EntityModel<Notificacion> model = notificacionModelAssembler.toModel(n);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/{id}")
    @Operation(summary="Buscar notificacion por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificacion encontrado"),
            @ApiResponse(responseCode = "404", description = "Notificacion no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Notificacion>> findById(@PathVariable Long id) {
        Notificacion n =  notificacionService.findById(id);
        return ResponseEntity.ok(notificacionModelAssembler.toModel(n));
    }

    @GetMapping("/user/{idUsuario}")
    @Operation(summary="Ver todas las notificaciones que ha recibido un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificacion encontrado"),
            @ApiResponse(responseCode = "404", description = "Notificacion no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<CollectionModel<EntityModel<Notificacion>>> findByUsuario(@PathVariable Long idUsuario) {
        List<Notificacion> notificacions = notificacionService.findByUsuario(idUsuario);
        if(notificacions.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<Notificacion>> models = notificacions.stream()
                .map(notificacionModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(NotificacionController.class).findAll()).withSelfRel()
                ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar una notificacion por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificacion eliminada"),
            @ApiResponse(responseCode = "404", description = "Notificacion no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        notificacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}