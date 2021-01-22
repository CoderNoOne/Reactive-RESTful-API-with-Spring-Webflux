package com.app.infrastructure.repository.impl;

import com.app.domain.security.User;
import com.app.domain.security.UserRepository;
import com.app.infrastructure.repository.mongo.MongoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final MongoUserRepository mongoUserRepository;

    @Override
    public Mono<User> findByUsername(String username) {
        return mongoUserRepository.findByUsername(username);
    }

    @Override
    public Mono<User> addOrUpdate(User user) {
        return mongoUserRepository.save(user);
    }

    @Override
    public Flux<User> addOrUpdateMany(List<User> users) {
        return mongoUserRepository.saveAll(users);
    }

    @Override
    public Flux<User> findAll() {
        return mongoUserRepository.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return mongoUserRepository.findById(id);
    }

    @Override
    public Flux<User> findAllById(List<String> ids) {
        return mongoUserRepository.findAllById(ids);
    }

    @Override
    public Mono<User> deleteById(String id) {
        return mongoUserRepository
                .findById(id)
                .flatMap(user ->
                        mongoUserRepository
                                .deleteById(id)
                                .then(Mono.just(user)));
    }

    @Override
    public Flux<User> deleteAllById(List<String> ids) {
        return mongoUserRepository
                .findAllById(ids)
                .collectList()
                .flatMap(users -> mongoUserRepository
                        .deleteAll(users)
                        .then(Mono.just(users)))
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return mongoUserRepository.findByEmail(email);
    }
}
