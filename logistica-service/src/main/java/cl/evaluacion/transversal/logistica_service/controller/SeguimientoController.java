package cl.evaluacion.transversal.logistica_service.controller;

import cl.evaluacion.transversal.logistica_service.assembler.SeguimientoModelAssembler;
import cl.evaluacion.transversal.logistica_service.dto.LogisticaRequest;
import cl.evaluacion.transversal.logistica_service.dto.LogisticaResponse;
import cl.evaluacion.transversal.logistica_service.model.Seguimiento;
import cl.evaluacion.transversal.logistica_service.service.SeguimientoService;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/seguimiento")
@Tag(name="Logistica y Seguimientos", description="Operaciones relacionadas a los seguimientos de pedidos")
public class SeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;

    @Autowired
    private SeguimientoModelAssembler seguimientoModelAssembler;

    @GetMapping("/")
    @Operation(summary="Ver todos los seguimientos")
    @ApiResponse(responseCode = "200", description = "Seguimientos encontrados")
    public ResponseEntity<CollectionModel<EntityModel<Seguimiento>>> findAll() {
        List<Seguimiento> seguimientos = seguimientoService.findAll();
        if(seguimientos.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Seguimiento>> models = seguimientos.stream()
                .map(seguimientoModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(SeguimientoController.class).findAll()).withSelfRel()
                ));
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary="Buscar seguimiento por su codigo unico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seguimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Seguimiento no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Seguimiento>> findByCodigo(@PathVariable String codigo) {
        Seguimiento s = seguimientoService.findByCodigo(codigo);
        return ResponseEntity.ok(seguimientoModelAssembler.toModel(s));
    }


    @PostMapping("/crear")
    @Operation(summary="Crear un seguimiento dentro de la base de datos de logistica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Seguimiento agregado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Seguimiento>> create(@Valid @RequestBody LogisticaRequest lr) {
        Seguimiento s = seguimientoService.save(lr);
        EntityModel<Seguimiento> model = seguimientoModelAssembler.toModel(s);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/{id}")
    @Operation(summary="Buscar un seguimiento por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seguimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Seguimiento no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Seguimiento>> findById(@PathVariable Long id) {
        Seguimiento s = seguimientoService.findById(id);
        return ResponseEntity.ok(seguimientoModelAssembler.toModel(s));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar un seguimiento por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Seguimiento eliminado"),
            @ApiResponse(responseCode = "404", description = "Seguimiento no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seguimientoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}