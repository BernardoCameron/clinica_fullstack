package com.duoc.clinica.clinica.Config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Clinica Medica")
                        .version("1.0.0")
                        .description("Proyecto API Clinica - Desarrollo FullStack.")
                        .contact(new Contact()
                                .name("Bernardo Cameron")
                                .email("be.cameron@duocuc.cl")
                        )
                );
    }

}
