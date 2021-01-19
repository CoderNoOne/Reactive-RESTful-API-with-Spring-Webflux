package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateTicketPurchaseDto;
import com.app.application.service.TicketPurchaseService;
import com.app.infrastructure.aspect.annotations.Loggable;
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
public class TicketPurchaseHandler {

    private final TicketPurchaseService ticketPurchaseService;

    @Loggable
    public Mono<ServerResponse> purchaseTicketFromOrder(ServerRequest serverRequest) {

        return serverRequest.principal()
                .flatMap(principal -> ticketPurchaseService.purchaseTicketFromOrder(principal.getName(), serverRequest.pathVariable("ticketOrderId")))
                .flatMap(ticketPurchase -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ticketPurchase))
                );
    }

    @Loggable
    public Mono<ServerResponse> purchaseTicket(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateTicketPurchaseDto.class)
                .flatMap(createTicketPurchaseDto -> ticketPurchaseService.purchaseTicket(serverRequest.principal(), createTicketPurchaseDto))
                .flatMap(ticketPurchase -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ticketPurchase))
                );
    }
}
