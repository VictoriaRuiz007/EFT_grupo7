package cl.evaluacion.transversal.pedido_service.controller;

import cl.evaluacion.transversal.pedido_service.assembler.PedidoModelAssembler;
import cl.evaluacion.transversal.pedido_service.dto.PedidoRequest;
import cl.evaluacion.transversal.pedido_service.dto.PedidoResponse;
import cl.evaluacion.transversal.pedido_service.model.Pedido;
import cl.evaluacion.transversal.pedido_service.service.PedidoService;
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
@RequestMapping("/api/pedidos")
@Tag(name="Pedidos", description = "Pedidos y operaciones sobre pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;

    @GetMapping("/")
    @Operation(summary="Ver todos los pedidos")
    @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> findAll() {
        List<Pedido> pedidos = pedidoService.findAll();
        if (pedidos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<Pedido>> models = pedidos.stream()
                .map(pedidoModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(PedidoController.class).findAll()).withSelfRel()
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary="Buscar un pedido por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Pedido>> findById(@PathVariable Long id) {
        Pedido p = pedidoService.findById(id);
        return ResponseEntity.ok(pedidoModelAssembler.toModel(p));
    }

    @PostMapping("/crear")
    @Operation(summary="Crear un pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Pedido creado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Pedido>> create(@Valid @RequestBody PedidoRequest pr) {
        Pedido p = pedidoService.create(pr);
        EntityModel<Pedido> model = pedidoModelAssembler.toModel(p);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar un pedido por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}