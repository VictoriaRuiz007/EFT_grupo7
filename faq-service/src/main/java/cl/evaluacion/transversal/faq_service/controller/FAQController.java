package cl.evaluacion.transversal.faq_service.controller;

import cl.evaluacion.transversal.faq_service.assembler.FAQModelAssembler;
import cl.evaluacion.transversal.faq_service.dto.FAQRequest;
import cl.evaluacion.transversal.faq_service.dto.FAQResponse;
import cl.evaluacion.transversal.faq_service.model.FAQ;
import cl.evaluacion.transversal.faq_service.service.FAQService;
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
@RequestMapping("/api/faq")
@Tag(name="FAQ", description="Frequently Asked Questions")
public class FAQController {

    @Autowired
    private FAQService faqService;

    @Autowired
    private FAQModelAssembler  faqModelAssembler;

    @GetMapping("/")
    @Operation(summary="Todas las FAQ")
    @ApiResponse(responseCode = "200", description = "FAQs encontradas: ")
    public ResponseEntity<CollectionModel<EntityModel<FAQ>>> findAll() {
        List<FAQ> faqs = faqService.findAll();
        if(faqs.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<FAQ>> models = faqs.stream()
                .map(faqModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(models,
                linkTo(methodOn(FAQController.class).findAll()).withSelfRel()));
    }

    @PostMapping
    @Operation(summary="Le permite crear una FAQ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "FAQ agregada"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<EntityModel<FAQ>> save(@Valid @RequestBody FAQRequest fr) {
        FAQ f = faqService.create(fr);
        EntityModel<FAQ> model = faqModelAssembler.toModel(f);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Le permite eliminar una FAQ usando su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "FAQ eliminada"),
            @ApiResponse(responseCode = "404", description = "FAQ no encontrado"),
            @ApiResponse(responseCode = "505", description = "Error interno del servidor! :b")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faqService.delete(id);
        return ResponseEntity.noContent().build();
    }
}