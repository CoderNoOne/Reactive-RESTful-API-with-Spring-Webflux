package com.app.application.service;

import com.app.domain.ticket_purchase.TicketPurchase;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TicketPurchaseService {

    private final TicketPurchaseRepository ticketPurchaseRepository;

    public Mono<TicketPurchase> purchaseTicket(){

        ticketPurchaseRepository.
    }
}
