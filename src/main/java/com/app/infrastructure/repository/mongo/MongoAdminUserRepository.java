package com.app.infrastructure.repository.mongo;

import com.app.domain.security.Admin;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoAdminUserRepository extends ReactiveMongoRepository<Admin, String> {
    Mono<Admin> findByUsername(String username);
}
