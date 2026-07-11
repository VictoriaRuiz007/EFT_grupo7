package cl.evaluacion.transversal.wishlist_service.controller;

import cl.evaluacion.transversal.wishlist_service.assembler.DeseoModelAssembler;
import cl.evaluacion.transversal.wishlist_service.dto.DeseoRequest;
import cl.evaluacion.transversal.wishlist_service.dto.DeseoResponse;
import cl.evaluacion.transversal.wishlist_service.model.Deseo;
import cl.evaluacion.transversal.wishlist_service.service.DeseoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name="Wishlists", description="Operaciones sobre listas de deseos de los usuarios")
public class DeseoController {

    @Autowired
    private DeseoService deseoService;

    @Autowired
    private DeseoModelAssembler deseoModelAssembler;

    @GetMapping("/usuario/{id}")
    @Operation(summary="Buscar wishlist de usuario especifico con su id de usuario")
    @ApiResponse(responseCode = "200", description = "Wishlis encontrada")
    public ResponseEntity<EntityModel<Deseo>> findByIdUsuario(@PathVariable Long id) {
        Deseo d = deseoService.listByUsuario(id);
        return ResponseEntity.ok(deseoModelAssembler.toModel(d));
    }

    @PostMapping("/crear")
    @Operation(summary="Crear deseo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Deseo creado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<Deseo>> create(@Valid @RequestBody DeseoRequest dr) {
        Deseo d = deseoService.save(dr);
        EntityModel<Deseo> model = deseoModelAssembler.toModel(d);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/usuario/{id}")
    @Operation(summary="Eliminar wishlist de usuario con su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wishlist eliminado"),
            @ApiResponse(responseCode = "404", description = "Wishlist no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        deseoService.deleteByUsuario(id);
        return ResponseEntity.noContent().build();
    }
}