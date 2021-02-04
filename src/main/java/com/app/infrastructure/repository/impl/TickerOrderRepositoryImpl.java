package com.app.infrastructure.repository.impl;

import com.app.domain.ticket_order.TicketOrder;
import com.app.domain.ticket_order.TicketOrderRepository;
import com.app.infrastructure.repository.mongo.MongoTicketOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TickerOrderRepositoryImpl implements TicketOrderRepository {

    private final MongoTicketOrderRepository mongoTicketOrderRepository;

    @Override
    public Mono<TicketOrder> addOrUpdate(TicketOrder item) {
        return mongoTicketOrderRepository.save(item);
    }

    @Override
    public Flux<TicketOrder> addOrUpdateMany(List<TicketOrder> items) {
        return mongoTicketOrderRepository.saveAll(items);
    }

    @Override
    public Flux<TicketOrder> findAll() {
        return mongoTicketOrderRepository.findAll();
    }

    @Override
    public Mono<TicketOrder> findById(String id) {
        return mongoTicketOrderRepository.findById(id);
    }

    @Override
    public Flux<TicketOrder> findAllById(List<String> ids) {
        return mongoTicketOrderRepository.findAllById(ids);
    }

    @Override
    public Mono<TicketOrder> deleteById(String id) {
        return mongoTicketOrderRepository
                .findById(id)
                .flatMap(ticketOrder -> mongoTicketOrderRepository
                        .delete(ticketOrder)
                        .then(Mono.just(ticketOrder)));
    }

    @Override
    public Flux<TicketOrder> deleteAllById(List<String> ids) {
        return mongoTicketOrderRepository
                .findAllById(ids)
                .collectList()
                .flatMap(ticketOrders -> mongoTicketOrderRepository
                        .deleteAll(ticketOrders)
                        .then(Mono.just(ticketOrders)))
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<TicketOrder> findAllByUsername(String username) {
        return mongoTicketOrderRepository.findAllByUserUsername(username);
    }
}
