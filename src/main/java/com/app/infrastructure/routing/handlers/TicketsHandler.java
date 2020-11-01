package com.app.infrastructure.routing.handlers;

import com.app.application.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TicketsHandler {

    private final TicketService ticketService;

//    public Mono<ServerResponse> buyTicket(final ServerRequest serverRequest) {
//
//        serverRequest.principal()
//                .map(principal -> ticketService.)
//
//
//        return null;
//    }
}
