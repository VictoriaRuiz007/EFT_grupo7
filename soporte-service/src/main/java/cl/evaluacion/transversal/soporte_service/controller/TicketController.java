package cl.evaluacion.transversal.soporte_service.controller;

import cl.evaluacion.transversal.soporte_service.assembler.TicketModelAssembler;
import cl.evaluacion.transversal.soporte_service.dto.TicketRequest;
import cl.evaluacion.transversal.soporte_service.dto.TicketResponse;
import cl.evaluacion.transversal.soporte_service.model.Ticket;
import cl.evaluacion.transversal.soporte_service.service.TicketService;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/soporte")
@Tag(name="Soporte", description="Registro de tickets enviados a soporte")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketModelAssembler ticketModelAssembler;

    @GetMapping("/")
    @Operation(summary="Ver todos los tickets")
    @ApiResponse(responseCode = "200", description = "Tickets encontrados")
    public ResponseEntity<CollectionModel<EntityModel<Ticket>>> findAll() {
        List<Ticket> tickets = ticketService.findAll();
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<Ticket>> models = tickets.stream()
                .map(ticketModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(TicketController.class).findAll()).withSelfRel()
                ));
    }

    @GetMapping("/{id}")
    @Operation(summary="Buscar ticket por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket encontrado"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Ticket>> findById(@PathVariable Long id) {
        Ticket t = ticketService.findById(id);
        return ResponseEntity.ok(ticketModelAssembler.toModel(t));
    }

    @PostMapping("/crear")
    @Operation(summary="Crear ticket para soporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Ticket creado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Ticket>> create(@Valid @RequestBody TicketRequest tr) {
        Ticket t =  ticketService.save(tr);
        EntityModel<Ticket> model = ticketModelAssembler.toModel(t);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar ticket por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket eliminado"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary="Filtrar tickets por estado(ABIERTO; CERRADO; EN PROCESO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets encontrados"),
            @ApiResponse(responseCode = "404", description = "Tickets no encontrados"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<CollectionModel<EntityModel<Ticket>>> findByEstado(@PathVariable String estado) {
        List<Ticket> tickets = ticketService.findByEstado(estado);
        if (tickets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EntityModel<Ticket>> models = tickets.stream()
                .map(ticketModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(TicketController.class).findByEstado(estado)).withSelfRel()
                ));
    }
}