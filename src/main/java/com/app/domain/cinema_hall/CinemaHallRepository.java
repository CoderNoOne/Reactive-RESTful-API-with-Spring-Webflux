package com.app.domain.cinema_hall;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Flux;

public interface CinemaHallRepository extends CrudRepository<CinemaHall, String> {

    Flux<CinemaHall> getAllForCinemaById(String cinemaId);
}
