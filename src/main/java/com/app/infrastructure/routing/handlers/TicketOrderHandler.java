package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateTicketOrderDto;
import com.app.application.service.TicketOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class TicketOrderHandler {

    private final TicketOrderService ticketOrderService;

    public Mono<ServerResponse> orderTickets(final ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateTicketOrderDto.class)
                .flatMap(value -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ticketOrderService.addTicketOrder(serverRequest.principal(), value))));
    }
}
