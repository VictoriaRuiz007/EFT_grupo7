package cl.evaluacion.transversal.wishlist_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API de wishlist-service")
                        .version("1.0")
                        .description("Documentacion API de wishlist para sistema de tienda de videojuegos")
                );
    }
}
