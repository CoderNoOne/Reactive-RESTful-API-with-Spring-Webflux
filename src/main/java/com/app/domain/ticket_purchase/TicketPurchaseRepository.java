package com.app.domain.ticket_purchase;

import com.app.domain.generic.CrudRepository;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface TicketPurchaseRepository extends CrudRepository<TicketPurchase, String> {

    Flux<TicketPurchase> findAllByUserUsername(String username);

    Flux<TicketPurchase> findAllByCinemaHallsIds(List<String> cinemaHallsIds);

    Flux<TicketPurchase> findAllByCinemaHallsIdsAndUsername(List<String> cinemaHallsIds, String username);

    Flux<TicketPurchase> findAllByPurchaseDateBetween(LocalDate from, LocalDate to);

    Flux<TicketPurchase> findAllByPurchaseDateAfter(LocalDate from);

    Flux<TicketPurchase> findAllByPurchaseDateBefore(LocalDate to);

    Flux<TicketPurchase> findAllByMovieId(String movieId);

    Flux<TicketPurchase> findAllByMovieIdAndUserUsername(String movieId, String username);

    Flux<TicketPurchase> findAllByCinemaHallId(String cinemaHallId);
}
