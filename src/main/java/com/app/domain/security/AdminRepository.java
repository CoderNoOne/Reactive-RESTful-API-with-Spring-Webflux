package com.app.domain.security;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Mono;

public interface AdminRepository extends CrudRepository<Admin, String> {
    Mono<Admin> findByUsername(String username);
}
