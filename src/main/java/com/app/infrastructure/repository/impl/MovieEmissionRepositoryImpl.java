package com.app.infrastructure.repository.impl;

import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.movie_emission.MovieEmissionRepository;
import com.app.infrastructure.repository.mongo.MongoMovieEmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieEmissionRepositoryImpl implements MovieEmissionRepository {

    private final MongoMovieEmissionRepository mongoMovieEmissionRepository;

    @Override
    public Mono<MovieEmission> addOrUpdate(MovieEmission item) {
        return mongoMovieEmissionRepository.save(item);
    }

    @Override
    public Flux<MovieEmission> addOrUpdateMany(List<MovieEmission> items) {
        return mongoMovieEmissionRepository.saveAll(items);
    }

    @Override
    public Flux<MovieEmission> findAll() {
        return mongoMovieEmissionRepository.findAll();
    }

    @Override
    public Mono<MovieEmission> findById(String id) {
        return mongoMovieEmissionRepository.findById(id);
    }

    @Override
    public Flux<MovieEmission> findAllById(List<String> ids) {
        return mongoMovieEmissionRepository.findAllById(ids);
    }

    @Override
    public Mono<MovieEmission> deleteById(String id) {
        return mongoMovieEmissionRepository
                .findById(id)
                .flatMap(movieEmission -> mongoMovieEmissionRepository
                        .delete(movieEmission)
                        .then(Mono.just(movieEmission)));
    }

    @Override
    public Flux<MovieEmission> deleteAllById(List<String> ids) {
        return mongoMovieEmissionRepository
                .findAllById(ids)
                .collectList()
                .flatMap(items -> mongoMovieEmissionRepository
                        .deleteAll(items)
                        .then(Mono.just(items)))
                .flatMapMany(Flux::fromIterable);

    }

    @Override
    public Flux<MovieEmission> findMovieEmissionsByMovieId(String movieId) {
        return mongoMovieEmissionRepository.findMovieEmissionsByMovieId(movieId);
    }
}
