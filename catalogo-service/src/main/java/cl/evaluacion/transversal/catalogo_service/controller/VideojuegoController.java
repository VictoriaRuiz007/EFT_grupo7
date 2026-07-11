package cl.evaluacion.transversal.catalogo_service.controller;

import cl.evaluacion.transversal.catalogo_service.assembler.VideojuegoModelAssembler;
import cl.evaluacion.transversal.catalogo_service.dto.VideojuegoRequest;
import cl.evaluacion.transversal.catalogo_service.dto.VideojuegoResponse;
import cl.evaluacion.transversal.catalogo_service.model.Videojuego;
import cl.evaluacion.transversal.catalogo_service.service.VideojuegoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/catalogo")
@Tag(name="Catalogo", description= "Operaciones relacionadas al catalogo")
public class VideojuegoController {

    @Autowired
    private VideojuegoService videojuegoService;

    @Autowired
    private VideojuegoModelAssembler videojuegoModelAssembler;

    @GetMapping(value = "/", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary="Obtener todos los productos")
    @ApiResponse(responseCode="200", description="Operacion exitosa")
    public ResponseEntity<CollectionModel<EntityModel<Videojuego>>> listarTodos() {
        List<Videojuego> videojuegos = videojuegoService.listAll();
        if(videojuegos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<EntityModel<Videojuego>> models= videojuegos.stream()
                .map(videojuegoModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(VideojuegoController.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary="Busca un producto por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Videojuego>> buscarPorId(@PathVariable Long id) {
        Videojuego v = videojuegoService.findById(id);
        return ResponseEntity.ok(videojuegoModelAssembler.toModel(v));
    }

    @PostMapping("/crear")
    @Operation(summary="Le permite agregar un producto al catalogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Producto agregado"),
            @ApiResponse(responseCode = "505", description = "Error interno de servidor")
    })
    public ResponseEntity<EntityModel<Videojuego>> crear(@Valid @RequestBody VideojuegoRequest vr) {
        Videojuego v = videojuegoService.save(vr);
        EntityModel<Videojuego> model = videojuegoModelAssembler.toModel(v);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto Eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    @Operation(summary="Le permite eliminar un producto del catalogo usando su id")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        videojuegoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}