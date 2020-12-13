package com.app.application.service;

import com.app.application.dto.CreateCityDto;
import com.app.domain.city.City;
import com.app.domain.city.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public Mono<City> addCity(Mono<CreateCityDto> createCityDto) {
        return createCityDto
                .flatMap(dto -> cityRepository
                .addOrUpdate(dto.toEntity()));
    }

    public Mono<City> findByName(String name){
        return cityRepository.findByName(name);
    }

    public Flux<City> getAll() {
        return cityRepository.findAll();
    }
}
