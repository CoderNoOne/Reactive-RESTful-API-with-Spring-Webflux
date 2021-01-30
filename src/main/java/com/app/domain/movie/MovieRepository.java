package com.app.domain.movie;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Mono;

public interface MovieRepository extends CrudRepository<Movie, String> {
    Mono<Movie> findByNameAndGenre(String name, String genre);

}
