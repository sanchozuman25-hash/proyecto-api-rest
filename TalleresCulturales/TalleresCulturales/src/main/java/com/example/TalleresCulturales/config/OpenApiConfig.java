package com.example.TalleresCulturales.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI talleresCulturalesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Talleres Culturales")
                        .version("1.0.0")
                        .description("API REST para gestionar los talleres de un centro cultural, "
                                + "sus organizadores y las personas inscritas.")
                        .contact(new Contact().name("Manuel")))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor local de desarrollo")));
    }
}
