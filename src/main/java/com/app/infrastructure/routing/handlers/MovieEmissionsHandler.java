package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateMovieEmissionDto;
import com.app.application.dto.MovieEmissionDto;
import com.app.application.dto.ResponseErrorDto;
import com.app.application.service.MovieEmissionService;
import com.app.infrastructure.aspect.annotations.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Loggable
    @Operation(
            summary = "POST add movie emission",
            requestBody = @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateMovieEmissionDto.class))),
            security = @SecurityRequirement(name = "JwtAuthToken"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Success", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = MovieEmissionDto.class)), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", description = "Error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))
            })

    })
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
