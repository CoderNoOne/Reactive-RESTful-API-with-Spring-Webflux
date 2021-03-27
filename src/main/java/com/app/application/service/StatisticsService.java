package com.app.application.service;

import com.app.domain.cinema.CinemaRepository;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.city.CityRepository;
import com.app.domain.ticket_order.TicketOrderRepository;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CinemaHallRepository cinemaHallRepository;
    private final TicketOrderRepository ticketOrderRepository;
    private final TicketPurchaseRepository ticketPurchaseRepository;
    private final CinemaRepository cinemaRepository;
    private final CityRepository cityRepository;

    public Flux<Map<String, Integer>> findCitiesFrequency() {

        final var currentDate = LocalDate.now();

        return cityRepository.findAll()
                .flatMap(city -> ticketPurchaseRepository.findAllByMovieEmissionInDateAndByCinemaHallsIdIn(
                        currentDate,
                        city.getCinemas()
                                .stream()
                                .flatMap(cinema -> cinema.getCinemaHalls().stream().map(CinemaHall::getId))
                                .collect(Collectors.toList()))
                        .map(ticketPurchase -> ticketPurchase.getTickets().size())
                        .map(ticketsNumber -> Map.of(city.getName(), ticketsNumber))
                );
    }

    public Flux<Map<String, Integer>> findCitiesWithMostFrequency() {

        final Flux<Map<String, Integer>> citiesFrequency = findCitiesFrequency();

        final Mono<Integer> maxFrequency = citiesFrequency
                .reduce((map1, map2) -> map1.values().iterator().next() > map2.values().iterator().next() ? map1 : map2)
                .map(dictionary -> dictionary.values().iterator().next());


        return citiesFrequency.sort(Comparator.comparing(map -> map.values().iterator().next(), Comparator.reverseOrder()))
                .filterWhen(dict -> maxFrequency.map(val -> val <= dict.values().iterator().next()));

    }


}
