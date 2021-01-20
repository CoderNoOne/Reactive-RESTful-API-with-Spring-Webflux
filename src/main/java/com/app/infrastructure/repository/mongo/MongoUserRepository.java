package com.app.infrastructure.repository.mongo;

import com.app.domain.movie.Movie;
import com.app.domain.security.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MongoUserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);

    @Query(value = "{'username': ?0}", fields = "{username : 0, birthDate: 0, password : 0, _id : 0, role : 0, favoriteMovies : 1}")
    Flux<Movie> sda(String username);
}
