package com.app.infrastructure.repository.mongo;

import com.app.domain.movie.Movie;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoMovieRepository extends ReactiveMongoRepository<Movie, String> {
}
