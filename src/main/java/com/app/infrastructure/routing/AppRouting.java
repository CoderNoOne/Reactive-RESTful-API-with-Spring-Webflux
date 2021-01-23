package com.app.infrastructure.routing;

import com.app.infrastructure.routing.handlers.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AppRouting {

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/register", beanClass = UsersHandler.class, beanMethod = "register"),
            @RouterOperation(path = "/users", beanClass = UsersHandler.class, beanMethod = "getAllUsers"),
            @RouterOperation(path = "/users/username/{username}", beanClass = UsersHandler.class, beanMethod = "getByUsername"),
            @RouterOperation(path = "/users/promoteToAdmin/username/{username}", beanClass = UsersHandler.class, beanMethod = "promoteUserToAdminRole")
    })
    public RouterFunction<ServerResponse> usersRoute(UsersHandler usersHandler) {
        return route(POST("/register").and(accept(MediaType.APPLICATION_JSON)), usersHandler::register)
                .andRoute(GET("/users").and(accept(MediaType.APPLICATION_JSON)), usersHandler::getAllUsers)
                .andRoute(GET("/users/username/{username}").and(accept(MediaType.APPLICATION_JSON)), usersHandler::getByUsername)
                .andRoute(POST("/users/promoteToAdmin/username/{username}").and(accept(MediaType.APPLICATION_JSON)), usersHandler::promoteUserToAdminRole);
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/login", beanClass = LoginHandler.class, beanMethod = "login")
    })
    public RouterFunction<ServerResponse> loginRoute(LoginHandler loginHandler) {
        return route(POST("/login").and(accept(MediaType.APPLICATION_JSON)), loginHandler::login);
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/emails/send/single", beanClass = EmailHandler.class, beanMethod = "sendSingleEmail"),
            @RouterOperation(path = "/emails/send/multiple", beanClass = EmailHandler.class, beanMethod = "sendMultipleEmails")
    })
    public RouterFunction<ServerResponse> emailsRoute(EmailHandler emailHandler) {
        return route(POST("/emails/send/single").and(accept(MediaType.APPLICATION_JSON)), emailHandler::sendSingleEmail)
                .andRoute(POST("/emails/send/multiple").and(accept(MediaType.APPLICATION_JSON)), emailHandler::sendMultipleEmails);
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/cinemaHalls", beanClass = CinemaHallsHandler.class, beanMethod = "getAll"),
            @RouterOperation(path = "/cinemaHalls/cinemaId/{cinemaId}", beanClass = CinemaHallsHandler.class, beanMethod = "getAllForCinema"),
            @RouterOperation(path = "/cinemaHalls/addToCinema/cinemaId/{cinemaId}", beanClass = CinemaHallsHandler.class, beanMethod = "addCinemaHallToCinema"),
    })
    public RouterFunction<ServerResponse> cinemaHallsRoute(CinemaHallsHandler cinemaHallsHandler) {
        return route(POST("/cinemaHalls/addToCinema/cinemaId/{cinemaId}").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::addCinemaHallToCinema)
                .andRoute(GET("/cinemaHalls//cinemaId/{cinemaId}").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::getAllForCinema)
                .andRoute(GET("/cinemaHalls/").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::getAll)
    }

    @Bean
    public RouterFunction<ServerResponse> routing(
            final UsersHandler usersHandler,
            final LoginHandler loginHandler,
            final MoviesHandler moviesHandler,
            final TicketOrderHandler ticketOrderHandler,
            final CinemasHandler cinemasHandler,
            final MovieEmissionsHandler movieEmissionsHandler,
            final CitiesHandler citiesHandler,
            final CinemaHallsHandler cinemaHallsHandler,
            final TicketPurchaseHandler ticketPurchaseHandler,
            final EmailHandler emailHandler
    ) {

        return nest(
                path("/security"),
                route(POST("/register")
                        .and(accept(MediaType.APPLICATION_JSON)), usersHandler::register))

                .andNest(
                        path("/login"),
                        route(POST("")
                                .and(accept(MediaType.APPLICATION_JSON)), loginHandler::login))

                .andNest(
                        path("/movies"),
                        route(GET("id/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getById)
                                .andRoute(PATCH("addToFavorites/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::addMovieToFavorites)
                                .andRoute(POST("").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::addMovieToDatabase)
                                .andRoute(DELETE("/id/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::deleteMovieById)
                                .andRoute(GET("").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getAllMovies)
                                .andRoute(GET("filter/premiereDate").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByPremiereDate)
                                .andRoute(GET("filter/duration").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByDuration)
                                .andRoute(GET("filter/name/{name}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByName)
                                .andRoute(GET("filter/genre/{genre}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByGenre)
                                .andRoute(GET("filter/keyword/{keyword}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByKeyword)
                                .andRoute(POST("/csv").and(accept(MediaType.MULTIPART_FORM_DATA)), moviesHandler::addMovieToDatabaseWithCsvFile)
                                .andRoute(GET("/favorites").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getFavoriteMovies)
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
                /*.andNest(path("/cinemaHalls"),
                        route(POST("addToCinema/cinemaId/{cinemaId}").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::addCinemaHallToCinema)
                                .andRoute(GET("/cinemaId/{cinemaId}").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::getAllForCinema)
                                .andRoute(GET("").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::getAll)
                )*/
                .andNest(path("/ticketPurchases"),
                        route(POST("/ticketOrderId/{ticketOrderId}").and(accept(MediaType.APPLICATION_JSON)), ticketPurchaseHandler::purchaseTicketFromOrder)
                                .andRoute(POST("").and(accept(MediaType.APPLICATION_JSON)), ticketPurchaseHandler::purchaseTicket)
                )
//                .andNest(path("/users"),
//                        route(GET("").and(accept(MediaType.APPLICATION_JSON)), usersHandler::getAllUsers)
//                                .andRoute(GET("/username/{username}").and(accept(MediaType.APPLICATION_JSON)), usersHandler::getByUsername)
//                                .andRoute(POST("/promoteToAdmin/username/{username}").and(accept(MediaType.APPLICATION_JSON)), usersHandler::promoteUserToAdminRole)
//                )
                .andNest(path("/emails"),
                        route(POST("/send/single").and(accept(MediaType.APPLICATION_JSON)), emailHandler::sendSingleEmail)
                                .andRoute(POST("/send/multiple").and(accept(MediaType.APPLICATION_JSON)), emailHandler::sendMultipleEmails));
    }

}
