package cl.evaluacion.transversal.promociones_service.controller;

import cl.evaluacion.transversal.promociones_service.assembler.PromocionModelAssembler;
import cl.evaluacion.transversal.promociones_service.dto.PromocionRequest;
import cl.evaluacion.transversal.promociones_service.dto.PromocionResponse;
import cl.evaluacion.transversal.promociones_service.model.Promocion;
import cl.evaluacion.transversal.promociones_service.service.PromocionService;
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
@RequestMapping("/api/promociones")
@Tag(name="Promociones", description = "Todas las promociones y operaciones")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @Autowired
    private PromocionModelAssembler  promocionModelAssembler;

    @GetMapping("/")
    @Operation(summary="Ver todas las promociones")
    public ResponseEntity<CollectionModel<EntityModel<Promocion>>> findAll() {
        List<Promocion> promocions = promocionService.findAll();
        if (promocions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Promocion>> models= promocions.stream()
                .map(promocionModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(PromocionController.class).findAll()).withSelfRel()
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary="Buscar promocion por su id")
    @ApiResponse(responseCode = "200", description = "Promociones encontradas")
    public ResponseEntity<EntityModel<Promocion>> findById(@PathVariable Long id) {
        Promocion p = promocionService.findById(id);
        return ResponseEntity.ok(promocionModelAssembler.toModel(p));
    }

    @PostMapping("/crear")
    @Operation(summary="Crear una promocion nueva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Promocion creada"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Promocion>> create(@Valid @RequestBody PromocionRequest pr) {
        Promocion p = promocionService.save(pr);
        EntityModel<Promocion> model = promocionModelAssembler.toModel(p);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar una promocion por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Promocion eliminada"),
            @ApiResponse(responseCode = "404", description = "Promocion no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        promocionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary="Buscar una promocion por su codigo unico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promocion encontrado"),
            @ApiResponse(responseCode = "404", description = "Promocion no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Promocion>> findByCodigo(@PathVariable String codigo) {
        Promocion p = promocionService.findByCodigo(codigo);
        return ResponseEntity.ok(promocionModelAssembler.toModel(p));
    }
}