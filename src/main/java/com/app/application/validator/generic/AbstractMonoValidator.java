package com.app.application.validator.generic;

import reactor.core.publisher.Mono;

import java.util.Map;

public abstract class AbstractMonoValidator<T> implements MonoValidator<T> {

    protected Mono<? extends Map<String, String>> putToMapAndReturn(Mono<? extends Map<String, String>> monoMap, Map.Entry<String, String> entry) {
        return monoMap.map(mapValue -> {
            mapValue.put(entry.getKey(), entry.getValue());
            return mapValue;
        });
    }
}
