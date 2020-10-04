package com.app.infrastructure.repository.impl;

import com.app.domain.security.Admin;
import com.app.domain.security.AdminRepository;
import com.app.infrastructure.repository.mongo.MongoAdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final MongoAdminUserRepository mongoAdminUserRepository;

    @Override
    public Mono<Admin> findByUsername(String username) {
        return mongoAdminUserRepository.findByUsername(username);
    }

    @Override
    public Mono<Admin> addOrUpdate(Admin item) {
        return mongoAdminUserRepository.save(item);
    }

    @Override
    public Flux<Admin> addOrUpdateMany(List<Admin> items) {
        return mongoAdminUserRepository.saveAll(items);
    }

    @Override
    public Flux<Admin> findAll() {
        return mongoAdminUserRepository.findAll();
    }

    @Override
    public Flux<Admin> findAllById(List<String> ids) {
        return mongoAdminUserRepository.findAllById(ids);
    }

    @Override
    public Mono<Admin> deleteById(String id) {
        return mongoAdminUserRepository
                .findById(id)
                .flatMap(user ->
                        mongoAdminUserRepository
                                .deleteById(id)
                                .then(Mono.just(user)));
    }

    @Override
    public Flux<Admin> deleteAllById(List<String> ids) {
        return mongoAdminUserRepository
                .findAllById(ids)
                .collectList()
                .flatMap(users -> mongoAdminUserRepository
                        .deleteAll(users)
                        .then(Mono.just(users)))
                .flatMapMany(Flux::fromIterable);
    }
}
