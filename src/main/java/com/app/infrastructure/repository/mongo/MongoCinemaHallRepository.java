package com.app.infrastructure.repository.mongo;

import com.app.domain.cinema_hall.CinemaHall;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MongoCinemaHallRepository extends ReactiveMongoRepository<CinemaHall, String> {

    @Query(value = "{'cinemaId': ?0}")
    Flux<CinemaHall> findByCinemaId(String cinemaId);

    @Query(value = "{'movieEmissions':{$elemMatch: {'id': ?0}}}")
    Mono<CinemaHall> findByMovieEmissionId(String id);

}
