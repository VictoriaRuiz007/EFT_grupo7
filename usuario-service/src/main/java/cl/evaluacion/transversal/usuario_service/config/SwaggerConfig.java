package cl.evaluacion.transversal.usuario_service.config;

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
                        .title("API de usuario-service")
                        .version("1.0")
                        .description("Documentacion API de usuario para sistema de tienda de videojuegos")
                );
    }
}
