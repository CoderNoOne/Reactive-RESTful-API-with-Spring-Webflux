package com.app.infrastructure.routing.handlers;

import com.app.application.dto.AddCinemaHallToCinemaDto;
import com.app.application.dto.CinemaHallDto;
import com.app.application.dto.ResponseDto;
import com.app.application.service.CinemaHallService;
import com.app.domain.cinema_hall.CinemaHall;
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
public class CinemaHallsHandler {

    private final CinemaHallService cinemaHallService;

    @Loggable
    public Mono<ServerResponse> addCinemaHallToCinema(ServerRequest serverRequest) {

        return cinemaHallService
                .addCinemaHallToCinema(serverRequest.bodyToMono(AddCinemaHallToCinemaDto.class))
                .flatMap(cinemaHallDto -> ServerResponse.status(HttpStatus.CREATED)
                        .body(BodyInserters.fromValue(cinemaHallDto))
                );

    }

    @Loggable
    public Mono<ServerResponse> getAllForCinema(ServerRequest serverRequest) {

        return cinemaHallService
                .getAllForCinema(serverRequest.pathVariable("cinemaId"))
                .collectList()
                .flatMap(list -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(list))
                );

    }

    @Loggable
    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {

        return cinemaHallService
                .getAll()
                .collectList()
                .flatMap(list -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ResponseDto.<List<CinemaHallDto>>builder().data(list).build())
                ));

    }
}
