package com.app.infrastructure.repository.mongo;

import com.app.domain.cinema_hall.CinemaHall;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoCinemaHallRepository extends ReactiveMongoRepository<CinemaHall, String> {

    @Query(value = "{'cinemaId': ?0}")
    Flux<CinemaHall> findByCinemaId(String cinemaId);

}
