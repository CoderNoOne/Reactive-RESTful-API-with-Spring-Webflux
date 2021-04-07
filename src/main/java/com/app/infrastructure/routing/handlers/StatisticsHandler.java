package com.app.infrastructure.routing.handlers;

import com.app.application.dto.MovieDto;
import com.app.application.dto.ResponseErrorDto;
import com.app.application.service.StatisticsService;
import com.app.infrastructure.aspect.annotations.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class StatisticsHandler {

    private final StatisticsService statisticsService;

    @Loggable
    @Operation(summary = "GET cinema frequency by city for all cities")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(type = "object", requiredProperties = {"city", "frequency"}))
            }),
            @ApiResponse(responseCode = "500", description = "Error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))
            })

    })
    public Mono<ServerResponse> getCinemaFrequencyByCityForAllCities(final ServerRequest serverRequest) {

        return statisticsService.findCitiesFrequency()
                .flatMap(list -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(list))
                );

    }

    @Loggable
    @Operation(summary = "GET city with max cinema frequency")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(type = "object", requiredProperties = {"city", "frequency"}))
            }),
            @ApiResponse(responseCode = "500", description = "Error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))
            })

    })
    public Mono<ServerResponse> getCinemaWithMaxFrequency(final ServerRequest serverRequest) {

        return statisticsService.findCitiesWithMostFrequency()
                .flatMap(list -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(list))
                );

    }

    @Loggable
    @Operation(summary = "GET most popular movie grouped by city")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(type = "object", requiredProperties = {"city", "frequency"}))
            }),
            @ApiResponse(responseCode = "500", description = "Error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))
            })

    })
    public Mono<ServerResponse> findMostPopularMovieGroupedByCity(final ServerRequest serverRequest) {

        return statisticsService.findMostPopularMovieGroupedByCity()
                .flatMap(map -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(map))
                );
    }

}