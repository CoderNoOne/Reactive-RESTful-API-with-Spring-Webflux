package com.app.infrastructure.repository.impl;

import com.app.domain.ticket.Ticket;
import com.app.domain.ticket.TicketRepository;
import com.app.infrastructure.repository.mongo.MongoTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TicketRepositoryImpl implements TicketRepository {

    private final MongoTicketRepository mongoTicketRepository;

    @Override
    public Mono<Ticket> addOrUpdate(Ticket item) {
        return mongoTicketRepository.save(item);
    }

    @Override
    public Flux<Ticket> addOrUpdateMany(List<Ticket> items) {
        return mongoTicketRepository.saveAll(items);
    }

    @Override
    public Flux<Ticket> findAll() {
        return mongoTicketRepository.findAll();
    }

    @Override
    public Mono<Ticket> findById(String id) {
        return mongoTicketRepository.findById(id);
    }

    @Override
    public Flux<Ticket> findAllById(List<String> ids) {
        return mongoTicketRepository.findAllById(ids);
    }

    @Override
    public Mono<Ticket> deleteById(String id) {
        return mongoTicketRepository
                .findById(id)
                .flatMap(ticket ->
                        mongoTicketRepository.delete(ticket)
                                .then(Mono.just(ticket)));
    }

    @Override
    public Flux<Ticket> deleteAllById(List<String> ids) {
        return mongoTicketRepository
                .findAllById(ids)
                .collectList()
                .flatMap(tickets -> mongoTicketRepository
                        .deleteAll(tickets)
                        .then(Mono.just(tickets)))
                .flatMapMany(Flux::fromIterable);
    }
}
