package com.app.infrastructure.routing;

import com.app.infrastructure.routing.handlers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AppRouting {

    @Bean
    public RouterFunction<ServerResponse> routing(
            final UsersHandler usersHandler,
            final SecurityHandler securityHandler,
            final MoviesHandler moviesHandler,
            final TicketOrderHandler ticketOrderHandler,
            final MovieEmissionsHandler movieEmissionsHandler
    ) {

        return RouterFunctions
                .nest(
                        path("/security"),
                        route(POST("/register")
                                .and(accept(MediaType.APPLICATION_JSON)), usersHandler::register))

                .andNest(
                        path("/login"),
                        route(POST("")
                                .and(accept(MediaType.APPLICATION_JSON)), securityHandler::login))

                .andNest(
                        path("/movies"),
                        route(GET("/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getById)
                                .andRoute(PATCH("addToFavorites/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::addMovieToFavorites)
                                .andRoute(POST("").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::addMovieToDatabase)
                                .andRoute(DELETE("/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::deleteMovieById)
                                .andRoute(GET("").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getAllMovies)
                                .andRoute(POST("/csv").and(accept(MediaType.MULTIPART_FORM_DATA)), moviesHandler::addMovieToDatabaseWithCsvFile)
                )

                .andNest(
                        path("/ticketOrders"),
                        route(POST("").and(accept(MediaType.APPLICATION_JSON)), ticketOrderHandler::orderTickets)
                )

                .andNest(
                        path("/movieEmissions"),
                        route(POST("").and(accept(MediaType.APPLICATION_JSON)), movieEmissionsHandler::addMovieEmission)
                );
    }

}
