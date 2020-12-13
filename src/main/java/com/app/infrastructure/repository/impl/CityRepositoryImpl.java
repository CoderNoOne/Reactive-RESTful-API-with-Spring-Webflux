package com.app.infrastructure.repository.impl;

import com.app.domain.city.City;
import com.app.domain.city.CityRepository;
import com.app.infrastructure.repository.mongo.MongoCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CityRepositoryImpl implements CityRepository {

    private final MongoCityRepository mongoCityRepository;

    @Override
    public Mono<City> addOrUpdate(City city) {
        return mongoCityRepository.save(city);
    }

    @Override
    public Flux<City> addOrUpdateMany(List<City> cities) {
        return mongoCityRepository.saveAll(cities);
    }

    @Override
    public Flux<City> findAll() {
        return mongoCityRepository.findAll();
    }

    @Override
    public Mono<City> findById(String id) {
        return mongoCityRepository.findById(id);
    }

    @Override
    public Flux<City> findAllById(List<String> ids) {
        return mongoCityRepository.findAllById(ids);
    }

    @Override
    public Mono<City> deleteById(String id) {
        return mongoCityRepository.findById(id)
                .flatMap(city -> mongoCityRepository
                        .delete(city)
                        .then(Mono.just(city))
                );
    }

    @Override
    public Flux<City> deleteAllById(List<String> ids) {
        return mongoCityRepository.findAllById(ids)
                .collectList()
                .flatMap(cities -> mongoCityRepository.deleteAll(cities)
                        .then(Mono.just(cities))
                ).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<City> findByName(String name) {
        return mongoCityRepository.findByName(name);
    }
}
