package com.app.infrastructure.routing;

import com.app.infrastructure.routing.handlers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
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
            final CinemasHandler cinemasHandler,
            final MovieEmissionsHandler movieEmissionsHandler,
            final CitiesHandler citiesHandler,
            final CinemaHallsHandler cinemaHallsHandler
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
                                .andRoute(GET("filter/premiereDate").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByPremiereDate)
                                .andRoute(GET("filter/duration").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByDuration)
                                .andRoute(GET("filter/name/{name}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByName)
                                .andRoute(GET("filter/genre/{genre}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByGenre)
                                .andRoute(GET("filter/keyword/{keyword}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByKeyword)
                                .andRoute(POST("/csv").and(accept(MediaType.MULTIPART_FORM_DATA)), moviesHandler::addMovieToDatabaseWithCsvFile)
                )

                .andNest(
                        path("/ticketOrders"),
                        route(POST("").and(accept(MediaType.APPLICATION_JSON)), ticketOrderHandler::orderTickets)
                )
                .andNest(
                        path("/cinemas"),
                        route(POST("").and(accept(MediaType.APPLICATION_JSON)), cinemasHandler::addCinema)
                                .andRoute(GET("").and(accept(MediaType.APPLICATION_JSON)), cinemasHandler::getAll)
                                .andRoute(GET("/city/{city}").and(accept(MediaType.APPLICATION_JSON)), cinemasHandler::getAllCinemasByCity)
                                .andRoute(PUT("/id/{id}/addCinemaHall").and(accept(MediaType.APPLICATION_JSON)), cinemasHandler::addCinemaHall)
                )

                .andNest(
                        path("/movieEmissions"),
                        route(POST("").and(accept(MediaType.APPLICATION_JSON)), movieEmissionsHandler::addMovieEmission)
                )
                .andNest(
                        path("/cities"),
                        route(POST("").and(accept(MediaType.APPLICATION_JSON)), citiesHandler::addCity)
                                .andRoute(GET("name/{name}").and(accept(MediaType.APPLICATION_JSON)), citiesHandler::findByName)
                                .andRoute(GET("").and(accept(MediaType.APPLICATION_JSON)), citiesHandler::getAll)
                                .andRoute(PUT("").and(accept(MediaType.APPLICATION_JSON)), citiesHandler::addCinemaToCity)
                )
                .andNest(path("/cinemaHalls"),
                        route(POST("addToCinema/cinemaId/{cinemaId}").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::addCinemaHallToCinema)
                                .andRoute(GET("/cinemaId/{cinemaId}").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::getAllForCinema)
                                .andRoute(GET("").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::getAll)
                );
    }

}
