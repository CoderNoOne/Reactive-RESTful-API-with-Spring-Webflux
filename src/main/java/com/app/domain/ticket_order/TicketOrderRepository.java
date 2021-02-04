package com.app.domain.ticket_order;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Flux;

public interface TicketOrderRepository extends CrudRepository<TicketOrder, String> {

    Flux<TicketOrder> findAllByUsername(String username);
}
