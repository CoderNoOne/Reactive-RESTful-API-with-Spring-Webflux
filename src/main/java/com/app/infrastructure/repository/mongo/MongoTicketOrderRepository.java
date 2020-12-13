package com.app.infrastructure.repository.mongo;

import com.app.domain.ticket_order.TicketOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoTicketOrderRepository extends ReactiveMongoRepository<TicketOrder, String> {
}
