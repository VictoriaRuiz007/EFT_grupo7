package cl.evaluacion.transversal.pago_service.controller;

import cl.evaluacion.transversal.pago_service.assembler.TransaccionModelAssembler;
import cl.evaluacion.transversal.pago_service.dto.TransaccionRequest;
import cl.evaluacion.transversal.pago_service.dto.TransaccionResponse;
import cl.evaluacion.transversal.pago_service.model.Transaccion;
import cl.evaluacion.transversal.pago_service.service.TransaccionService;
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
@RequestMapping("/api/pago")
@Tag(name="Transacciones", description = "Registro y operaciones sobre transacciones y pagos")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private TransaccionModelAssembler  transaccionModelAssembler;

    @GetMapping("/")
    @Operation(summary="Ver todas las transacciones realizadas")
    @ApiResponse(responseCode = "200", description = "Transacciones encontradas")
    public ResponseEntity<CollectionModel<EntityModel<Transaccion>>> findAll() {
        List<Transaccion> transacciones = transaccionService.findAll();
        if(transacciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Transaccion>> models = transacciones.stream()
                .map(transaccionModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(TransaccionController.class).findAll()).withSelfRel()
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary="Buscar una transaccion por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaccion encontrado"),
            @ApiResponse(responseCode = "404", description = "Transaccion no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Transaccion>> findById(@PathVariable Long id) {
        Transaccion t = transaccionService.findById(id);
        return ResponseEntity.ok(transaccionModelAssembler.toModel(t));
    }

    @PostMapping("/crear")
    @Operation(summary="Crear un registro de transaccion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Transaccion creada"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Transaccion>> crear(@Valid @RequestBody TransaccionRequest tr) {
        Transaccion t = transaccionService.save(tr);
        EntityModel<Transaccion> model = transaccionModelAssembler.toModel(t);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar una transaccion del sistema por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaccion eliminada"),
            @ApiResponse(responseCode = "404", description = "Transaccion no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        transaccionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pedido/{id}")
    @Operation(summary="Buscar la transaccion de un pedido por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaccion encontrado"),
            @ApiResponse(responseCode = "404", description = "Transaccion no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Transaccion>> findByIdPedido(@PathVariable Long id) {
        Transaccion t = transaccionService.findByIdPedido(id);
        return ResponseEntity.ok(transaccionModelAssembler.toModel(t));
    }
}