package com.app.infrastructure.repository.impl;

import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.infrastructure.repository.mongo.MongoCinemaHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CinemaHallRepositoryImpl implements CinemaHallRepository {

    private final MongoCinemaHallRepository mongoCinemaHallRepository;

    @Override
    public Mono<CinemaHall> addOrUpdate(CinemaHall item) {
        return mongoCinemaHallRepository.save(item);
    }

    @Override
    public Flux<CinemaHall> addOrUpdateMany(List<CinemaHall> items) {
        return mongoCinemaHallRepository.saveAll(items);
    }

    @Override
    public Flux<CinemaHall> findAll() {
        return mongoCinemaHallRepository.findAll();
    }

    @Override
    public Mono<CinemaHall> findById(String id) {
        return mongoCinemaHallRepository.findById(id);
    }

    @Override
    public Flux<CinemaHall> findAllById(List<String> ids) {
        return mongoCinemaHallRepository.findAllById(ids);
    }

    @Override
    public Mono<CinemaHall> deleteById(String id) {
        return mongoCinemaHallRepository
                .findById(id)
                .flatMap(cinemaHall -> mongoCinemaHallRepository
                        .delete(cinemaHall)
                        .then(Mono.just(cinemaHall)));
    }

    @Override
    public Flux<CinemaHall> deleteAllById(List<String> ids) {
        return mongoCinemaHallRepository
                .findAllById(ids)
                .collectList()
                .flatMap(list -> mongoCinemaHallRepository
                        .deleteAll(list)
                        .then(Mono.just(list)))
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<CinemaHall> getAllForCinemaById(String cinemaId) {
        return mongoCinemaHallRepository.findByCinemaId(cinemaId);
    }
}
