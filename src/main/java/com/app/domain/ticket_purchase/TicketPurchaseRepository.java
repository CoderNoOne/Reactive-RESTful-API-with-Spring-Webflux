package com.app.domain.ticket_purchase;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Flux;

public interface TicketPurchaseRepository extends CrudRepository<TicketPurchase, String> {

    Flux<TicketPurchase> findAllByUserUsername(String username);
}
