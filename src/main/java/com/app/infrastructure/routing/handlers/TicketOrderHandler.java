package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateTicketOrderDto;
import com.app.application.service.TicketOrderService;
import com.app.infrastructure.aspect.annotations.Loggable;
import com.mongodb.internal.connection.Server;
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
public class TicketOrderHandler {

    private final TicketOrderService ticketOrderService;

    @Loggable
    public Mono<ServerResponse> orderTickets(final ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateTicketOrderDto.class)
                .flatMap(value -> ticketOrderService.addTicketOrder(serverRequest.principal(), value))
                .flatMap(value -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(value)));
    }

    @Loggable
    public Mono<ServerResponse> cancelOrder(ServerRequest serverRequest) {

        return serverRequest.principal()
                .flatMap(principal -> ticketOrderService.cancelOrder(principal.getName(), serverRequest.pathVariable("orderId")))
                .flatMap(ticketOrder -> ServerResponse
                        .status(200)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ticketOrder))
                );

    }
}
