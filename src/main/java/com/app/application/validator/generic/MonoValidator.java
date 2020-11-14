package com.app.application.validator.generic;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface MonoValidator<T> {

    Mono<? extends Map<String, String>> validate(T item);
}
