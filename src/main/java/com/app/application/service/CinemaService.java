package com.app.application.service;

import com.app.application.dto.CinemaDto;
import com.app.application.dto.CreateCinemaDto;
import com.app.application.service.util.ServiceUtils;
import com.app.domain.cinema.Cinema;
import com.app.domain.cinema.CinemaRepository;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.city.City;
import com.app.domain.city.CityRepository;
import com.app.domain.vo.Position;
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

        return cinemaHallRepository
                .addOrUpdateMany(createCinemaDto
                        .getCinemaHallsCapacity().stream()
                        .map(dtoVal -> CinemaHall.builder()
                                .positions(ServiceUtils.buildPositions(dtoVal.getPositionNumbers()))
                                .movieEmissions(Collections.emptyList())
                                .build())
                        .collect(Collectors.toList()))
                .collectList()
                .flatMap(cinemaHalls -> cinemaRepository.addOrUpdate(Cinema.builder()
                        .cinemaHalls(cinemaHalls)
                        .street(createCinemaDto.getStreet())
                        .build()))
                .flatMap(cinema ->
                        cityRepository.findByName(createCinemaDto.getCity())
                                .switchIfEmpty(cityRepository.addOrUpdate(
                                        City.builder()
                                                .name(createCinemaDto.getCity())
                                                .build()))
                                .flatMap(city -> {
                                            cinema.setCity(city.getName());
                                            cinema.getCinemaHalls()
                                                    .forEach(cinemaHall -> cinemaHall.setCinemaId(cinema.getId()));

                                            if (isNull(city.getCinemas())) {
                                                city.setCinemas(new ArrayList<>());
                                            }
                                            city.getCinemas().add(cinema);
                                            return cinemaHallRepository.addOrUpdateMany(cinema.getCinemaHalls())
                                                    .then(cityRepository.addOrUpdate(city))
                                                    .then(cinemaRepository.addOrUpdate(cinema));
                                        }
                                )
                ).as(transactionalOperator::transactional);
    }

    public Flux<CinemaDto> getAll() {
        return cinemaRepository.findAll()
                .map(Cinema::toDto);
    }

    public Flux<CinemaDto> getAllByCity(String city) {
        return cinemaRepository.findAllByCity(city)
                .map(Cinema::toDto);
    }
}
