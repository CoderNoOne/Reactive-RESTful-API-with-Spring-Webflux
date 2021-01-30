package com.app.domain.movie_emission;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Flux;

public interface MovieEmissionRepository extends CrudRepository<MovieEmission, String> {

    Flux<MovieEmission> findMovieEmissionsByMovieId(String movieId);

    Flux<MovieEmission> findMovieEmissionsByCinemaHallId(String cinemaHallId);
}
