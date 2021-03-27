package com.app.infrastructure.repository.impl;

import com.app.domain.ticket_purchase.TicketPurchase;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import com.app.infrastructure.repository.mongo.MongoTicketPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class TicketPurchaseRepositoryImpl implements TicketPurchaseRepository {

    private final MongoTicketPurchaseRepository mongoTicketPurchaseRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

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

    @Override
    public Flux<TicketPurchase> findAllByUserUsername(String username) {
        return mongoTicketPurchaseRepository.findAllByUserUsername(username);
    }

    @Override
    public Flux<TicketPurchase> findAllByCinemaHallsIds(List<String> cinemaHallsIds) {
        return mongoTicketPurchaseRepository.findAllByMovieEmissionCinemaHallIdIn(cinemaHallsIds);
    }

    @Override
    public Flux<TicketPurchase> findAllByCinemaHallsIdsAndUsername(List<String> cinemaHallsIds, String username) {
        return mongoTicketPurchaseRepository.findAllByMovieEmissionCinemaHallIdAndUserUsername(cinemaHallsIds, username);
    }

    @Override
    public Flux<TicketPurchase> findAllByPurchaseDateBetween(LocalDate from, LocalDate to) {
        return mongoTicketPurchaseRepository.findAllByPurchaseDateBetween(from, to);
    }

    @Override
    public Flux<TicketPurchase> findAllByPurchaseDateAfter(LocalDate from) {
        return mongoTicketPurchaseRepository.findAllByPurchaseDateAfter(from);
    }

    @Override
    public Flux<TicketPurchase> findAllByPurchaseDateBefore(LocalDate to) {
        return mongoTicketPurchaseRepository.findAllByPurchaseDateBefore(to);
    }

    @Override
    public Flux<TicketPurchase> findAllByMovieId(String movieId) {
        return mongoTicketPurchaseRepository.findAllByMovieEmissionMovieId(movieId);
    }

    @Override
    public Flux<TicketPurchase> findAllByMovieIdAndUserUsername(String movieId, String username) {
        return mongoTicketPurchaseRepository.findAllByMovieEmissionMovieIdAndUserUsername(movieId, username);
    }

    @Override
    public Flux<TicketPurchase> findAllByCinemaHallId(String cinemaHallId) {
        return mongoTicketPurchaseRepository.findAllByMovieEmissionCinemaHallId(cinemaHallId);
    }

    @Override
    public Flux<TicketPurchase> findAllByMovieEmissionInDateAndByCinemaHallsIdIn(LocalDate beforeDate, List<String> cinemaHallIds) {

        return reactiveMongoTemplate
                .find(new Query(Criteria
                        .where("movieEmission")
                        .elemMatch(Criteria
                                .where("startDateTime")
                                .lt(LocalDate.now().atTime(0, 0)))
                        .elemMatch(Criteria
                                .where("cinemaHallsId")
                                .in(cinemaHallIds)
                        )), TicketPurchase.class);
    }

}
