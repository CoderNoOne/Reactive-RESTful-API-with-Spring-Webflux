package com.app.domain.cinema;

import com.app.domain.generic.CrudRepository;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CinemaRepository extends CrudRepository<Cinema, String> {

    Mono<Cinema> findByCinemaHallId(String id);

    Flux<Cinema> findAllByCity(String city);
}
