package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateMovieDto;
import com.app.application.dto.ErrorMessageDto;
import com.app.application.dto.ExceptionResponseDto;
import com.app.application.dto.ResponseDto;
import com.app.application.exception.AuthenticationException;
import com.app.application.exception.RegistrationUserException;
import com.app.application.service.MovieService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.internal.connection.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MoviesHandler {

    private final MovieService movieService;

    public Mono<ServerResponse> addMovieToFavorites(final ServerRequest serverRequest) {

        return serverRequest.principal()
                .map(principal -> movieService.addMovieToFavorites(serverRequest.pathVariable("id"), principal.getName()))
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie)))
                .onErrorResume(AuthenticationException.class, ex -> ServerResponse
                        .status(401)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(
                                ResponseDto.builder()
                                        .error(ErrorMessageDto.builder()
                                                .message(ex.getMessage())
                                                .build())
                                        .build()
                        )));

    }

    public Mono<ServerResponse> getById(final ServerRequest serverRequest) {

        return movieService.getById(serverRequest.pathVariable("id"))
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .body(BodyInserters
                                .fromValue(ResponseDto.builder()
                                        .error(ErrorMessageDto.builder()
                                                .message("No Movie with id: %s".formatted(serverRequest.pathVariable("id")))
                                                .build())
                                        .build())))
                .onErrorResume(
                        AuthenticationException.class,
                        e -> ServerResponse
                                .status(HttpStatus.FORBIDDEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(ResponseDto
                                        .builder()
                                        .error(ErrorMessageDto.builder()
                                                .message(e.getMessage())
                                                .build())
                                        .build()))
                );
    }

    public Mono<ServerResponse> addMovieToDatabase(final ServerRequest serverRequest) {

        return movieService.addMovie(serverRequest.bodyToMono(CreateMovieDto.class))
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie))
                );
    }

    public Mono<ServerResponse> deleteMovieById(final ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");


        return movieService.deleteMovieById(serverRequest.pathVariable("id"))
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie))
                );
    }

    public Mono<ServerResponse> getAllMovies(ServerRequest serverRequest) {
        return movieService.getAll()
                .collectList()
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie))
                );
    }
}