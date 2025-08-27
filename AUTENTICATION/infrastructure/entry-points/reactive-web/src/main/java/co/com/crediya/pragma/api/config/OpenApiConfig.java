package co.com.crediya.pragma.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CrediYa Autentication")
                        .description("API para el registro de usuarios")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Backend Microservicio")
                                .email("pragma@pragma.com")));
    }
}
