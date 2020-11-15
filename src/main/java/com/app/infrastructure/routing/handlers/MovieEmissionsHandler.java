package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateMovieEmissionDto;
import com.app.application.service.MovieEmissionService;
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
public class MovieEmissionsHandler {

    private final MovieEmissionService movieEmissionService;

    public Mono<ServerResponse> addMovieEmission(ServerRequest serverRequest) {

        return movieEmissionService
                .createMovieEmission(serverRequest.bodyToMono(CreateMovieEmissionDto.class))
                .flatMap(savedVal -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(savedVal))
                );
    }

}
