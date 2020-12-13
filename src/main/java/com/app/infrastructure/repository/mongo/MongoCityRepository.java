package com.app.infrastructure.repository.mongo;

import com.app.domain.city.City;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoCityRepository extends ReactiveMongoRepository<City, String> {
    @Query("{'name':?0}")
    Mono<City> findByName(String name);


}
