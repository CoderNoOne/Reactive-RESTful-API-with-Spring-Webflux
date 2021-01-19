package com.app.infrastructure.repository.impl;

import com.app.domain.ticket_purchase.TicketPurchase;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import com.app.infrastructure.repository.mongo.MongoTicketOrderRepository;
import com.app.infrastructure.repository.mongo.MongoTicketPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TicketPurchaseRepositoryImpl implements TicketPurchaseRepository {

    private final MongoTicketPurchaseRepository mongoTicketPurchaseRepository;

    @Override
    public Mono<TicketPurchase> addOrUpdate(TicketPurchase item) {
        return mongoTicketPurchaseRepository.save(item);
    }

    @Override
    public Flux<TicketPurchase> addOrUpdateMany(List<TicketPurchase> items) {
        return mongoTicketPurchaseRepository.saveAll(items);
    }

    @Override
    public Flux<TicketPurchase> findAll() {
        return mongoTicketPurchaseRepository.findAll();
    }

    @Override
    public Mono<TicketPurchase> findById(String id) {
        return mongoTicketPurchaseRepository.findById(id);
    }

    @Override
    public Flux<TicketPurchase> findAllById(List<String> ids) {
        return mongoTicketPurchaseRepository.findAllById(ids);
    }

    @Override
    public Mono<TicketPurchase> deleteById(String id) {
        return mongoTicketPurchaseRepository.findById(id)
                .flatMap(ticketPurchase -> mongoTicketPurchaseRepository.delete(ticketPurchase)
                        .then(Mono.just(ticketPurchase)));
    }

    @Override
    public Flux<TicketPurchase> deleteAllById(List<String> ids) {
        return mongoTicketPurchaseRepository
                .findAllById(ids)
                .collectList()
                .flatMap(ticketPurchases -> mongoTicketPurchaseRepository
                        .deleteAll(ticketPurchases)
                        .then(Mono.just(ticketPurchases)))
                .flatMapMany(Flux::fromIterable);
    }
}
