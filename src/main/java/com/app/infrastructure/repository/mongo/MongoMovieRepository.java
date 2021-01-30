package com.app.infrastructure.repository.mongo;

import com.app.domain.movie.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoMovieRepository extends ReactiveMongoRepository<Movie, String> {
    Mono<Movie> findByNameAndGenre(String name, String genre);
}
