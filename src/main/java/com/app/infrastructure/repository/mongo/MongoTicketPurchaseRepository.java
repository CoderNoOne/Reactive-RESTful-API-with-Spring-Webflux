package com.app.infrastructure.repository.mongo;

import com.app.domain.ticket_purchase.TicketPurchase;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

public interface MongoTicketPurchaseRepository extends ReactiveMongoRepository<TicketPurchase, String> {

    Flux<TicketPurchase> findAllByUserUsername(String username);

    Flux<TicketPurchase> findAllByMovieEmissionCinemaHallIdIn(List<String> cinemaHallsIds);

    Flux<TicketPurchase> findAllByMovieEmissionCinemaHallIdAndUserUsername(List<String> cinemaHallsIds, String username);

    Flux<TicketPurchase> findAllByPurchaseDateBetween(LocalDate from, LocalDate to);

    Flux<TicketPurchase> findAllByPurchaseDateAfter(LocalDate from);

    Flux<TicketPurchase> findAllByPurchaseDateBefore(LocalDate to);

    Flux<TicketPurchase> findAllByMovieEmissionMovieId(String movieId);

    Flux<TicketPurchase> findAllByMovieEmissionMovieIdAndUserUsername(String movieId, String username);

    Flux<TicketPurchase> findAllByMovieEmissionCinemaHallId(String cinemaHallId);

}
