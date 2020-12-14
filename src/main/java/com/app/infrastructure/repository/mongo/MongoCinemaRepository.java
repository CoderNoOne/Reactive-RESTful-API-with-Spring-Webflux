package com.app.infrastructure.repository.mongo;

import com.app.domain.cinema.Cinema;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MongoCinemaRepository extends ReactiveMongoRepository<Cinema, String> {

    @Query(value = "{'cinemaHalls':{$elemMatch: {'id': 0?}}}")
    Mono<Cinema> findByCinemaHallId(String id);

    @Query("{'city': ?0}")
    Flux<Cinema> getAllByCity(String city);
}
