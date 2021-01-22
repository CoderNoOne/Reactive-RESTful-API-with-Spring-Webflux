package com.app.domain.security;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends CrudRepository<User, String> {
    Mono<User> findByUsername(String username);

    Mono<User> findByEmail(String email);
}

