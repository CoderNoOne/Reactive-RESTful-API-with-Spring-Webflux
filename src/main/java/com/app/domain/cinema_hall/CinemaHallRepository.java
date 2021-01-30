package com.app.domain.cinema_hall;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CinemaHallRepository extends CrudRepository<CinemaHall, String> {

    Flux<CinemaHall> getAllForCinemaById(String cinemaId);

    Mono<CinemaHall> getByMovieEmissionId(String movieEmissionId);
}
