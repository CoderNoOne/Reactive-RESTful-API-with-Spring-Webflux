package com.app.infrastructure.openapi;

import com.app.CinemaApplication;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI openAPI() throws UnknownHostException {
        return new OpenAPI()
                .components(
                        new Components()
                                .securitySchemes(Map.of(
                                        "JwtAuthToken",
                                        new SecurityScheme()
                                                .bearerFormat("jwt")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer"))))
                .info(new Info()
                        .description("""
                                "Instance created at: " %s
                                 on host: %s
                                                                
                                """.formatted(CinemaApplication.INSTANCE_CREATION_TIME.get().toString(), InetAddress.getLocalHost().getHostAddress()))
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


