package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateMovieEmissionDto;
import com.app.application.service.MovieEmissionService;
import com.app.infrastructure.aspect.annotations.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MovieEmissionsHandler {


    private final MovieEmissionService movieEmissionService;

    @Loggable
    public Mono<ServerResponse> addMovieEmission(ServerRequest serverRequest) {

       return serverRequest.bodyToMono(CreateMovieEmissionDto.class)
                .flatMap(movieEmissionService::createMovieEmission)
                .flatMap(savedVal -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(savedVal))
                );

    }

}
