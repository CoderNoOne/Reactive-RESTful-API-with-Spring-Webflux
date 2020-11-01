package com.app.infrastructure.repository.mongo;

import com.app.domain.movie_emission.MovieEmission;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoMovieEmissionRepository extends ReactiveMongoRepository<MovieEmission, String> {
}
