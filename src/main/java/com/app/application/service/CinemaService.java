package com.app.application.service;

import com.app.application.dto.CreateCinemaDto;
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

        return cinemaHallRepository.addOrUpdateMany(createCinemaDto
                .getCinemaHallsCapacity().stream()
                .map(dtoVal -> CinemaHall.builder()
                        .positions(buildPositions(dtoVal.getPositionNumbers()))
                        .movieEmissions(Collections.emptyList())
                        .build())
                .collect(Collectors.toList()))
                .collectList()
                .flatMap(cinemaHalls -> cinemaRepository.addOrUpdate(Cinema.builder()
                        .cinemaHalls(cinemaHalls)
                        .build()))
                .flatMap(cinema ->
                        cityRepository.findByName(createCinemaDto.getCity())
                                .switchIfEmpty(cityRepository.addOrUpdate(
                                        City.builder()
                                                .name(createCinemaDto.getCity())
                                                .build()))
                                .flatMap(city -> {
                                            cinema.setCity(city.getName());
                                            if (isNull(city.getCinemas())) {
                                                city.setCinemas(new ArrayList<>());
                                            }
                                            city.getCinemas().add(cinema);
                                            return cityRepository.addOrUpdate(city)
                                                    .then(cinemaRepository.addOrUpdate(cinema));
                                        }
                                )
                ).as(transactionalOperator::transactional);
    }

    private List<Position> buildPositions(Integer positionNumber) {
        int sqrt = (int) Math.sqrt(positionNumber);
        var positions = new ArrayList<Position>();

        int rest = positionNumber - sqrt * sqrt;

        int i1 = rest / sqrt; /*tyle dodatkowych  pełnych rzędów*/

        int i2 = rest % sqrt;/*tyle miejsc zajętych w ostanim rzedzie*/

        for (int i = 1; i <= sqrt; i++) {
            for (int j = 1; j <= sqrt + i1; j++) {
                positions.add(Position.builder()
                        .rowNo(j)
                        .colNo(i)
                        .build());
            }
        }

        for (int i = 1; i <= i2; i++) {
            positions.add(Position.builder()
                    .rowNo(sqrt + i1 + 1)
                    .colNo(i)
                    .build());

        }

        System.out.println(positions);

//        long count = IntStream.rangeClosed(1, sqrt)
//                .boxed()
//                .map(positionNo ->
//                        IntStream.rangeClosed(positionNo, sqrt)
//                                .boxed()
//                                .map(position ->
//                                        IntStream.rangeClosed(position, sqrt)
//                                                .boxed()
//                                                .peek(pos -> positions.add(Position.builder()
//                                                        .rowNo(positionNo)
//                                                        .colNo(sqrt)
//                                                        .build()))
//                                )
//                                .map()
//
//                ).count();

        return positions;
    }

    public Flux<Cinema> getAll() {
        return cinemaRepository.findAll();
    }

    public Flux<Cinema> getAllByCity(String city) {
        return cinemaRepository.findAllByCity(city);
    }
}
