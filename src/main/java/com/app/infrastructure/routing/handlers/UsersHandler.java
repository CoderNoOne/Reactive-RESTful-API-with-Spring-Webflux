package com.app.infrastructure.routing.handlers;

import com.app.application.dto.*;
import com.app.application.exception.RegistrationUserException;
import com.app.application.service.UsersService;
import com.app.domain.security.User;
import com.app.infrastructure.aspect.annotations.Loggable;
import com.mongodb.lang.NonNullApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class UsersHandler {
    private final UsersService usersService;

    @Loggable
    @Operation(summary = "POST register user", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CreateUserDto.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "user saved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))
            })

    })
    public Mono<ServerResponse> register(ServerRequest serverRequest) {

        return serverRequest
                .bodyToMono(CreateUserDto.class)
                .switchIfEmpty(Mono.error(() -> new RegistrationUserException("No body found")))
                .flatMap(createUserDto -> usersService
                        .register(createUserDto)
                        .flatMap(response -> ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(response))));

    }

    @Loggable
    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {

        return usersService
                .getAll()
                .collectList()
                .flatMap(createdUser -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(createdUser)));
    }

    @Loggable
    public Mono<ServerResponse> getByUsername(ServerRequest serverRequest) {
        return usersService.getByUsername(serverRequest.pathVariable("username"))
                .flatMap(user -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(user))
                );
    }

    @Loggable
    public Mono<ServerResponse> promoteUserToAdminRole(ServerRequest serverRequest) {
        return usersService.promoteUserToAdminRole(serverRequest.pathVariable("username"))
                .flatMap(user -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(user))
                );
    }
}
