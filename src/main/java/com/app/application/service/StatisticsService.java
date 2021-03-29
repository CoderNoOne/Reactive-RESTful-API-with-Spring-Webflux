package com.app.application.service;

import com.app.application.dto.MovieFrequencyDto;
import com.app.domain.cinema.CinemaRepository;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.city.CityRepository;
import com.app.domain.movie.MovieRepository;
import com.app.domain.movie_emission.MovieEmissionRepository;
import com.app.domain.ticket_order.TicketOrderRepository;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CinemaHallRepository cinemaHallRepository;
    private final TicketOrderRepository ticketOrderRepository;
    private final TicketPurchaseRepository ticketPurchaseRepository;
    private final CinemaRepository cinemaRepository;
    private final CityRepository cityRepository;
    private final MovieRepository movieRepository;
    private final MovieEmissionRepository movieEmissionRepository;

    public Mono<Map<String, Integer>> findCitiesFrequency() {

        final var currentDate = LocalDate.now();

        return cityRepository.findAll()
                .flatMap(city -> ticketPurchaseRepository.findAllByMovieEmissionInDateAndByCinemaHallsIdIn(
                        currentDate,
                        city.getCinemas()
                                .stream()
                                .flatMap(cinema -> cinema.getCinemaHalls().stream().map(CinemaHall::getId))
                                .collect(Collectors.toList()))
                        .map(ticketPurchase -> ticketPurchase.getTickets().size())
                        .map(ticketsNumber -> Map.of(city.getName(), ticketsNumber)))
                .reduce(Collections.emptyMap(), (subMap1, subMap2) -> {
                    subMap1.putAll(subMap2);
                    return subMap1;
                });
    }

    public Mono<Map<String, Integer>> findCitiesWithMostFrequency() {

        final Mono<Map<String, Integer>> citiesFrequency = findCitiesFrequency();

        final Mono<Integer> maxFrequency = citiesFrequency
                .map(dict -> Collections.max(dict.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue());

        return maxFrequency
                .flatMap(maxValue -> citiesFrequency
                        .map(dict ->
                                dict.entrySet()
                                        .stream()
                                        .filter(e -> e.getValue().equals(maxValue))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                );
    }

    public Flux<MovieFrequencyDto> findAllMoviesFrequency() {

        return movieRepository.findAll()
                .flatMap(movie -> ticketPurchaseRepository.findAllByMovieId(movie.getId())
                        .collectList()
                        .map(tickets -> MovieFrequencyDto.builder().movie(movie.toDto()).frequency(tickets.stream().map(ticketPurchase -> ticketPurchase.getTickets().size()).reduce(0, Integer::sum)).build())
                );

    }

    public Mono<Map<String, List<MovieFrequencyDto>>> findMostPopularMovieGroupedByCity() {

        return cityRepository.findAll()
                .flatMap(city -> cinemaRepository.findAllByCity(city.getName())
                        .flatMap(cinema -> cinemaHallRepository.getAllForCinemaById(cinema.getId()))
                        .flatMap(cinemaHall -> movieEmissionRepository.findMovieEmissionsByCinemaHallId(cinemaHall.getId()))
                        .flatMap(movieEmission -> ticketPurchaseRepository.findAllByMovieId(movieEmission.getMovie().getId())
                                .collectMultimap(ticketPurchase -> ticketPurchase.getMovieEmission().getMovie(), ticketPurchase -> ticketPurchase.getTickets().size())
                                .map(dict -> {
                                            AtomicInteger maxVal = new AtomicInteger(0);
                                            AtomicInteger numberOfSameMaxVal = new AtomicInteger(0);
                                            return dict.entrySet()
                                                    .stream()
                                                    .collect(Collectors.collectingAndThen(Collectors.toMap(
                                                            Map.Entry::getKey,
                                                            e -> e.getValue().size(),
                                                            Integer::sum,
                                                            LinkedHashMap::new),
                                                            linkedMap -> linkedMap.entrySet()
                                                                    .stream()
                                                                    .peek(e -> maxVal.set(e.getValue() > maxVal.get() ? e.getValue() : maxVal.get()))
                                                                    .sorted()
                                                                    .limit(numberOfSameMaxVal.get())
                                                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
                                        }
                                ))
                        .map(movieFrequencyMap -> Map
                                .of(city.getName(),
                                        movieFrequencyMap.entrySet().stream()
                                                .map(e -> MovieFrequencyDto.builder().movie(e.getKey().toDto()).frequency(e.getValue()).build())
                                                .collect(Collectors.toList()))
                        ))
                .reduce(Collections.emptyMap(), (subMap1, subMap2) -> {
                    subMap1.putAll(subMap2);
                    return subMap1;
                });

    }


}
