package com.app.infrastructure.repository.mongo;

import com.app.domain.movie_emission.MovieEmission;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MongoMovieEmissionRepository extends ReactiveMongoRepository<MovieEmission, String> {

    Flux<MovieEmission> findMovieEmissionsByMovieId(String movieId);

    Flux<MovieEmission> findMovieEmissionsByCinemaHallId(String cinemaHallId);
}
