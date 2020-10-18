package com.app.infrastructure.routing.handlers;

import com.app.application.service.MovieService;
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
public class MoviesHandler {

    private final MovieService movieService;

    public Mono<ServerResponse> addMovieToFavorites(ServerRequest serverRequest) {

        return serverRequest.principal()
                .map(principal -> movieService.addMovieToFavorites(serverRequest.pathVariable("id"), principal.getName()))
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie)));

    }
}