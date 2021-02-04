package com.app.infrastructure.repository.mongo;

import com.app.domain.ticket_order.TicketOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoTicketOrderRepository extends ReactiveMongoRepository<TicketOrder, String> {

    Flux<TicketOrder> findAllByUserUsername(String username);
}
