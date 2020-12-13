package com.app.domain.city;

import com.app.domain.generic.CrudRepository;
import reactor.core.publisher.Mono;

public interface CityRepository extends CrudRepository<City, String> {

    Mono<City> findByName(String name);
}
