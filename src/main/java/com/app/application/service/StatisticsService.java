package com.app.application.service;

import com.app.domain.cinema.Cinema;
import com.app.domain.cinema.CinemaRepository;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.city.City;
import com.app.domain.ticket_order.TicketOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CinemaHallRepository cinemaHallRepository;
    private final TicketOrderRepository ticketOrderRepository;
    private final CinemaRepository cinemaRepository;

    //miejscowosc w której najwiecej osob chodzi do kina
    public Flux<Cinema> findCityWithMostFrequency() {

        return ticketOrderRepository
                .findAll()
                .collectMap(ticketOrder -> ticketOrder.getMovieEmission().getCinemaHall(),
                        ticketOrder -> ticketOrder.getTickets().size())
                .map(map -> {

                    if (map.isEmpty()) {
                        return map;
                    }

                    Integer maxValue = map.entrySet()
                            .stream()
                            .reduce(new ArrayList<>(map.entrySet()).get(0), (entry1, entry2) -> entry1.getValue() > entry2.getValue() ? entry1 : entry2)
                            .getValue();

                    return map.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().equals(maxValue))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                })
                .flatMapIterable(Map::keySet)
                .flatMap(cinemaHall -> cinemaRepository.findByCinemaHallId(cinemaHall.getId()));
    }


}
