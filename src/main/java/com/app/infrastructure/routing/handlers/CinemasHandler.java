package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CinemaDto;
import com.app.application.dto.CreateCinemaDto;
import com.app.application.dto.CreateCinemaHallDto;
import com.app.application.dto.ResponseDto;
import com.app.application.service.CinemaService;
import com.app.infrastructure.aspect.annotations.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CinemasHandler {

    private final CinemaService cinemaService;

    @Loggable
    public Mono<ServerResponse> addCinema(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateCinemaDto.class)
                .flatMap(cinemaService::addCinema)
                .flatMap(savedCinema -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(savedCinema))
                );
    }

    @Loggable
    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {

        return cinemaService.getAll()
                .collectList()
                .flatMap(cinemas -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ResponseDto.<List<CinemaDto>>builder().data(cinemas).build()))
                );
    }

    @Loggable
    public Mono<ServerResponse> getAllCinemasByCity(ServerRequest serverRequest) {

        return cinemaService.getAllByCity(serverRequest.pathVariable("city"))
                .collectList()
                .flatMap(cinemas -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ResponseDto.<List<CinemaDto>>builder().data(cinemas).build()))
                );
    }

    @Loggable
    public Mono<ServerResponse> addCinemaHall(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateCinemaHallDto.class)
                .flatMap(createCinemaHallDto -> cinemaService.addCinemaHallToCinema(serverRequest.pathVariable("id"), createCinemaHallDto))
                .flatMap(cinemaDto -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(cinemaDto))
                );
    }
}
