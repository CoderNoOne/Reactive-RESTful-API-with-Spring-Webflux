package com.app.infrastructure.repository.mongo;

import com.app.domain.ticket.Ticket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoTicketRepository extends ReactiveMongoRepository <Ticket, String> {
}
