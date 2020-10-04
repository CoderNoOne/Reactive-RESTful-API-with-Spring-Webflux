package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateUserDto;
import com.app.application.service.UsersService;
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

    public Mono<ServerResponse> register(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(CreateUserDto.class)
                .flatMap(createUserDto -> usersService
                        .register(createUserDto)
                        .flatMap(createdUserId -> ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(createdUserId)))
                );
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {

        return usersService
                .getAll()
                .collectList()
                .flatMap(createdUser -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(createdUser)));
    }
}
