package com.app.domain.cinema;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Mono;

public interface CinemaRepository extends CrudRepository<Cinema, String> {

    Mono<Cinema> findByCinemaHallId(String id);
}
