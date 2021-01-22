package com.app.infrastructure.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI() {{
            setExternalDocs(new ExternalDocumentation().url(""));
        }}
                .components(
                        new Components()
                                .securitySchemes(Map.of(
                                        "JwtAuthToken",
                                        new SecurityScheme()
                                                .bearerFormat("jwt")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer"))))
                .info(new Info()
                        .title("Cinema DDD webflux API")
                        .version("1.0")
                        .contact(new Contact()
                                .email("firelight.code@gmail.com")
                                .name("CoderNoOne")
                                .url("http://www.github.com/CoderNoOne")
                        )
                );

    }
}


