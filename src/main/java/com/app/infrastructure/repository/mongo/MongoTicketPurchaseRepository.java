package com.app.infrastructure.repository.mongo;

import com.app.domain.ticket_purchase.TicketPurchase;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoTicketPurchaseRepository extends ReactiveMongoRepository<TicketPurchase, String> {

    Flux<TicketPurchase> findAllByUserUsername(String username);
}
