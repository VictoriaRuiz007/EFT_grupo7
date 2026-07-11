package cl.evaluacion.transversal.faq_service.assembler;

import cl.evaluacion.transversal.faq_service.controller.FAQController;
import cl.evaluacion.transversal.faq_service.model.FAQ;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FAQModelAssembler implements RepresentationModelAssembler<FAQ, EntityModel<FAQ>> {

    @Override
    public EntityModel<FAQ> toModel(FAQ faq) {
        return EntityModel.of(faq,
                linkTo(methodOn(FAQController.class).findAll()).withRel("faqs")
                );
    }
}
