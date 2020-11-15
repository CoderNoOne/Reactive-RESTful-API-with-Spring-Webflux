package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateMovieDto;
import com.app.application.dto.ErrorMessageDto;
import com.app.application.dto.ResponseDto;
import com.app.application.exception.AuthenticationException;
import com.app.application.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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

    public Mono<ServerResponse> addMovieToDatabaseWithCsvFile(final ServerRequest serverRequest) {

        return movieService.uploadCSVFile(serverRequest.bodyToMono(Resource.class))
                .collectList()
                .flatMap(addedMovieList -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(addedMovieList))
                );
    }


    public Mono<ServerResponse> deleteMovieById(final ServerRequest serverRequest) {

        return movieService.deleteMovieById(serverRequest.pathVariable(serverRequest.pathVariable("id")))
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