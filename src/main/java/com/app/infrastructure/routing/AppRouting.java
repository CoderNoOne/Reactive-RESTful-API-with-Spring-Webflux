package com.app.infrastructure.routing;

import com.app.infrastructure.routing.handlers.*;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
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
                .andRoute(GET("/cinemaHalls/").and(accept(MediaType.APPLICATION_JSON)), cinemaHallsHandler::getAll);
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/movies/id/{id}", beanClass = MoviesHandler.class, beanMethod = "getById"),
            @RouterOperation(path = "/movies/addToFavorites/{id}", beanClass = MoviesHandler.class, beanMethod = "addMovieToFavorites"),
            @RouterOperation(path = "/movies", beanClass = MoviesHandler.class, beanMethod = "addMovieToDatabase"),
            @RouterOperation(path = "/movies/id/{id}", beanClass = MoviesHandler.class, beanMethod = "deleteMovieById"),
            @RouterOperation(path = "/movies", beanClass = MoviesHandler.class, beanMethod = "getAllMovies"),
            @RouterOperation(path = "/movies/filter/premiereDate", beanClass = MoviesHandler.class, beanMethod = "getMoviesFilteredByPremiereDate"),
            @RouterOperation(path = "/movies/filter/duration", beanClass = MoviesHandler.class, beanMethod = "getMoviesFilteredByDuration"),
            @RouterOperation(path = "/movies/filter/name/{name}", beanClass = MoviesHandler.class, beanMethod = "getMoviesFilteredByName"),
            @RouterOperation(path = "/movies/filter/genre/{genre}", beanClass = MoviesHandler.class, beanMethod = "getMoviesFilteredByGenre"),
            @RouterOperation(path = "/movies/filter/keyword/{keyword}", beanClass = MoviesHandler.class, beanMethod = "getMoviesFilteredByKeyword"),
            @RouterOperation(path = "/movies/csv", beanClass = MoviesHandler.class, beanMethod = "addMovieToDatabaseWithCsvFile"),
            @RouterOperation(path = "/movies/favorites", beanClass = MoviesHandler.class, beanMethod = "getFavoriteMovies")
    })
    public RouterFunction<ServerResponse> moviesRoute(MoviesHandler moviesHandler) {
        return route(GET("/movies/id/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getById)
                .andRoute(PATCH("/movies/addToFavorites/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::addMovieToFavorites)
                .andRoute(POST("/movies").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::addMovieToDatabase)
                .andRoute(DELETE("/movies/id/{id}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::deleteMovieById)
                .andRoute(GET("/movies").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getAllMovies)
                .andRoute(GET("/movies/filter/premiereDate").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByPremiereDate)
                .andRoute(GET("/movies/filter/duration").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByDuration)
                .andRoute(GET("/movies/filter/name/{name}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByName)
                .andRoute(GET("/movies/filter/genre/{genre}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByGenre)
                .andRoute(GET("/movies/filter/keyword/{keyword}").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getMoviesFilteredByKeyword)
                .andRoute(POST("/movies/csv").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::addMovieToDatabaseWithCsvFile)
                .andRoute(GET("/movies/favorites").and(accept(MediaType.APPLICATION_JSON)), moviesHandler::getFavoriteMovies);
    }

    @RouterOperations({
            @RouterOperation(path = "/ticketOrders", beanClass = TicketOrderHandler.class, beanMethod = "orderTickets")
    })
    @Bean
    public RouterFunction<ServerResponse> tickerOrdersRouting(TicketOrderHandler ticketOrderHandler) {
        return route(POST("/ticketOrders").and(accept(MediaType.APPLICATION_JSON)), ticketOrderHandler::orderTickets);
    }

    @RouterOperations({
            @RouterOperation(path = "/cinemas", beanClass = CinemasHandler.class, beanMethod = "addCinema"),
            @RouterOperation(path = "/cinemas", beanClass = CinemasHandler.class, beanMethod = "getAll"),
            @RouterOperation(path = "/cinemas/city/{cityId}", beanClass = CinemasHandler.class, beanMethod = "getAllCinemasByCity"),
            @RouterOperation(path = "/cinemas/id/{id}/addCinemaHall", beanClass = CinemasHandler.class, beanMethod = "addCinemaHall"),
    })
    @Bean
    public RouterFunction<ServerResponse> cinemasRouting(CinemasHandler cinemasHandler) {
        return route(POST("/cinemas").and(accept(MediaType.APPLICATION_JSON)), cinemasHandler::addCinema)
                .andRoute(GET("/cinemas").and(accept(MediaType.APPLICATION_JSON)), cinemasHandler::getAll)
                .andRoute(GET("/cinemas/city/{city}").and(accept(MediaType.APPLICATION_JSON)), cinemasHandler::getAllCinemasByCity)
                .andRoute(PUT("/cinemas/id/{id}/addCinemaHall").and(accept(MediaType.APPLICATION_JSON)), cinemasHandler::addCinemaHall);

    }

    @RouterOperations({
            @RouterOperation(path = "/movieEmissions", beanClass = MovieEmissionsHandler.class, beanMethod = "addMovieEmission")
    })
    @Bean
    public RouterFunction<ServerResponse> movieEmissionsRouting(MovieEmissionsHandler movieEmissionsHandler) {
        return route(POST("/movieEmissions").and(accept(MediaType.APPLICATION_JSON)), movieEmissionsHandler::addMovieEmission);
    }

    @RouterOperations({
            @RouterOperation(method = RequestMethod.POST, path = "/cinemas", beanClass = CitiesHandler.class, beanMethod = "addCity"),
            @RouterOperation(method = RequestMethod.GET, path = "/cinemas/name/{name}", beanClass = CitiesHandler.class, beanMethod = "findByName"),
            @RouterOperation(method = RequestMethod.GET, path = "/cinemas", beanClass = CitiesHandler.class, beanMethod = "getAll"),
            @RouterOperation(method = RequestMethod.PUT, path = "/cinemas", beanClass = CitiesHandler.class, beanMethod = "getAll")
    })
    @Bean
    public RouterFunction<ServerResponse> citiesRouting(CitiesHandler citiesHandler) {
        return route(POST("").and(accept(MediaType.APPLICATION_JSON)), citiesHandler::addCity)
                .andRoute(GET("name/{name}").and(accept(MediaType.APPLICATION_JSON)), citiesHandler::findByName)
                .andRoute(GET("").and(accept(MediaType.APPLICATION_JSON)), citiesHandler::getAll)
                .andRoute(PUT("").and(accept(MediaType.APPLICATION_JSON)), citiesHandler::addCinemaToCity);
    }

    @Bean
    @RouterOperations(
            {
                    @RouterOperation(path = "/ticketPurchases/ticketOrderId/{ticketOrderId}", beanClass = TicketPurchaseHandler.class, beanMethod = "purchaseTicketFromOrder"),
                    @RouterOperation(path = "/ticketPurchases", beanClass = TicketPurchaseHandler.class, beanMethod = "purchaseTicket")
            }
    )
    public RouterFunction<ServerResponse> ticketPurchasesRouting(TicketPurchaseHandler ticketPurchaseHandler) {
        return route(POST("/ticketPurchases/ticketOrderId/{ticketOrderId}").and(accept(MediaType.APPLICATION_JSON)), ticketPurchaseHandler::purchaseTicketFromOrder)
                .andRoute(POST("/ticketPurchases").and(accept(MediaType.APPLICATION_JSON)), ticketPurchaseHandler::purchaseTicket);
    }

}