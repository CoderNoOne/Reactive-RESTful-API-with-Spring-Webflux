package com.app.infrastructure.repository.mongo;

import com.app.domain.cinema_hall.CinemaHall;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoCinemaHallRepository extends ReactiveMongoRepository<CinemaHall, String> {
}
