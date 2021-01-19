package com.app.infrastructure.routing.handlers;

import com.app.application.dto.*;
import com.app.application.exception.AuthenticationException;
import com.app.application.service.MovieService;
import com.app.infrastructure.aspect.annotations.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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

    @Loggable
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

    @Loggable
    public Mono<ServerResponse> getById(final ServerRequest serverRequest) {

        return movieService.getById(serverRequest.pathVariable("id"))
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie)))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
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

    @Loggable
    public Mono<ServerResponse> addMovieToDatabase(final ServerRequest serverRequest) {

        return movieService.addMovie(serverRequest.bodyToMono(CreateMovieDto.class))
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie))
                );
    }

    @Loggable
    public Mono<ServerResponse> addMovieToDatabaseWithCsvFile(final ServerRequest serverRequest) {

        return movieService.uploadCSVFile(serverRequest.bodyToMono(Resource.class))
                .collectList()
                .flatMap(addedMovieList -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(addedMovieList))
                );
    }

    @Loggable
    public Mono<ServerResponse> deleteMovieById(final ServerRequest serverRequest) {

        return movieService.deleteMovieById(serverRequest.pathVariable(serverRequest.pathVariable("id")))
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie))
                );
    }

    @Loggable
    public Mono<ServerResponse> getAllMovies(ServerRequest serverRequest) {
        return movieService.getAll()
                .collectList()
                .flatMap(movie -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(movie))
                );
    }

    @Loggable
    public Mono<ServerResponse> getMoviesFilteredByPremiereDate(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(MovieFilteredByPremiereDate.class)
                .flatMap(dto -> movieService.getFilteredByPremiereDate(dto.getDateFrom(), dto.getDateTo())
                        .collectList()
                        .flatMap(movie -> ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(movie))
                        ));

    }

    @Loggable
    public Mono<ServerResponse> getMoviesFilteredByDuration(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(MovieFilteredByDuration.class)
                .flatMap(dto -> movieService.getFilteredByDuration(dto.getMinDuration(), dto.getMaxDuration())
                        .collectList()
                        .flatMap(movie -> ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(movie))
                        ));

    }

    @Loggable
    public Mono<ServerResponse> getMoviesFilteredByName(ServerRequest serverRequest) {

        return
                movieService.getFilteredByName(serverRequest.pathVariable("name"))
                        .collectList()
                        .flatMap(movie -> ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(movie))
                        );

    }

    @Loggable
    public Mono<ServerResponse> getMoviesFilteredByGenre(ServerRequest serverRequest) {

        return
                movieService.getFilteredByGenre(serverRequest.pathVariable("genre"))
                        .collectList()
                        .flatMap(movie -> ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(movie))
                        );

    }

    @Loggable
    public Mono<ServerResponse> getMoviesFilteredByKeyword(ServerRequest serverRequest) {

        return
                movieService.getFilteredByKeyword(serverRequest.pathVariable("keyword"))
                        .collectList()
                        .flatMap(movie -> ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(movie))
                        );

    }
}