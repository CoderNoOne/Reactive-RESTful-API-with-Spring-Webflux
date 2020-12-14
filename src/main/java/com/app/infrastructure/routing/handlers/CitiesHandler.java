package com.app.infrastructure.routing.handlers;

import com.app.application.dto.*;
import com.app.application.service.CityService;
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
public class CitiesHandler {

    private final CityService cityService;

    @Loggable
    public Mono<ServerResponse> addCity(ServerRequest request) {

        return cityService.addCity(request.bodyToMono(CreateCityDto.class))
                .flatMap(savedCity -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(savedCity))
                );
    }

    @Loggable
    public Mono<ServerResponse> findByName(ServerRequest request) {

        return cityService.findByName(request.pathVariable("name"))
                .flatMap(city -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ResponseDto.<CityDto>builder().data(city).build())))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .body(BodyInserters
                                .fromValue(ResponseDto.builder()
                                        .error(ErrorMessageDto.builder()
                                                .message("No city with name: %s".formatted(request.pathVariable("name")))
                                                .build())
                                        .build())));
    }

    @Loggable
    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {

        return cityService.getAll()
                .collectList()
                .flatMap(cities -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ResponseDto.<List<CityDto>>builder().data(cities).build())));
    }

    @Loggable
    public Mono<ServerResponse> addCinemaToCity(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(AddCinemaToCityDto.class)
                .flatMap(dto -> cityService.addCinemaToCity(dto)
                        .flatMap(city -> ServerResponse.status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(ResponseDto.<CityDto>builder().data(city).build())))

                );
    }
}
