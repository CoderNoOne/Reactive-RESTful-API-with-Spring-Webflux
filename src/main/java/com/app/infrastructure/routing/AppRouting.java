package com.app.infrastructure.routing;

import com.app.infrastructure.routing.handlers.SecurityHandler;
import com.app.infrastructure.routing.handlers.UsersHandler;
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
            UsersHandler usersHandler, SecurityHandler securityHandler) {

        return RouterFunctions
                .nest(
                        path("/security"),
                        route(POST("/register").and(accept(MediaType.APPLICATION_JSON)), usersHandler::register))

                .andNest(
                        path("/login"),
                        route(POST("").and(accept(MediaType.APPLICATION_JSON)), securityHandler::login));
    }

}
