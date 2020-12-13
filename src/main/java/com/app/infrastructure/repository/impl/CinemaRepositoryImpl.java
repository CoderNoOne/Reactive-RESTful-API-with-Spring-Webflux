package com.app.infrastructure.repository.impl;

import com.app.domain.cinema.Cinema;
import com.app.domain.cinema.CinemaRepository;
import com.app.infrastructure.repository.mongo.MongoCinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CinemaRepositoryImpl implements CinemaRepository {

    private final MongoCinemaRepository mongoCinemaRepository;

    @Override
    public Mono<Cinema> findByCinemaHallId(String id) {
        return mongoCinemaRepository.findByCinemaHallId(id);
    }

    @Override
    public Mono<Cinema> addOrUpdate(Cinema cinema) {
        return mongoCinemaRepository.save(cinema);
    }

    @Override
    public Flux<Cinema> addOrUpdateMany(List<Cinema> cinemas) {
        return mongoCinemaRepository.saveAll(cinemas);
    }

    @Override
    public Flux<Cinema> findAll() {
        return mongoCinemaRepository.findAll();
    }

    @Override
    public Mono<Cinema> findById(String id) {
        return mongoCinemaRepository.findById(id);
    }

    @Override
    public Flux<Cinema> findAllById(List<String> ids) {
        return mongoCinemaRepository.findAllById(ids);
    }

    @Override
    public Mono<Cinema> deleteById(String id) {
        return mongoCinemaRepository
                .findById(id)
                .flatMap(cinema -> mongoCinemaRepository
                        .delete(cinema)
                        .then(Mono.just(cinema)));
    }

    @Override
    public Flux<Cinema> deleteAllById(List<String> ids) {
        return mongoCinemaRepository
                .findAllById(ids)
                .collectList()
                .flatMap(cinemas ->
                        mongoCinemaRepository.deleteAll(cinemas).then(Mono.just(cinemas)))
                .flatMapMany(Flux::fromIterable);
    }
}
