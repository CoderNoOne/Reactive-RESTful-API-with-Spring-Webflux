package com.app.infrastructure.openapi;

import com.app.application.dto.CreateUserDto;
import com.app.application.dto.ResponseDto;
import com.app.application.dto.UserDto;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.fn.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.blockhound.shaded.net.bytebuddy.implementation.bytecode.assign.TypeCasting;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .securitySchemes(Map.of(
                                        "JwtAuthToken",
                                        new SecurityScheme()
                                                .bearerFormat("jwt")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer"))))
                .security(List.of(new SecurityRequirement().addList("JwtAuthToken")))
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

    @Bean
    public GroupedOpenApi securityApi() {
        return GroupedOpenApi.builder()
                .group("users-handler")
                .addOpenApiCustomiser(openApi -> openApi
                        .security(Collections.emptyList()))
                .build();
    }
}


