package com.app.infrastructure.repository.mongo;

import com.app.domain.security.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoRegularUserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String username);

}
