package com.app.application.service;

import com.app.application.dto.CreateCinemaDto;
import com.app.domain.cinema.Cinema;
import com.app.domain.cinema.CinemaRepository;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.city.City;
import com.app.domain.city.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;
    private final CinemaHallRepository cinemaHallRepository;
    private final CityRepository cityRepository;

    private final TransactionalOperator transactionalOperator;

    public Mono<Cinema> addCinema(CreateCinemaDto createCinemaDto) {

        return cinemaHallRepository.addOrUpdateMany(createCinemaDto
                .getCinemaHalls().stream()
                .map(dtoVal -> CinemaHall.builder()
                        .positions(dtoVal.getPositions())
                        .movieEmissions(Collections.emptyList())
                        .build())
                .collect(Collectors.toList()))
                .collectList()
                .flatMap(cinemaHalls -> cinemaRepository.addOrUpdate(Cinema.builder()
                        .cinemaHalls(cinemaHalls)
                        .build()))
                .flatMap(cinema ->
                        cityRepository.findByName(createCinemaDto.getCityName())
                                .switchIfEmpty(cityRepository.addOrUpdate(
                                        City.builder()
                                                .name(createCinemaDto.getCityName())
                                                .build()))
                                .flatMap(city -> {
                                            cinema.setCityName(city.getName());
                                            if (isNull(city.getCinemas())) {
                                                city.setCinemas(new ArrayList<>());
                                            }
                                            city.getCinemas().add(cinema);
                                            return cityRepository.addOrUpdate(city).then(Mono.just(cinema));
                                        }
                                )
                ).as(transactionalOperator::transactional);
    }

    public Flux<Cinema> getAll() {
        return cinemaRepository.findAll();
    }
}
