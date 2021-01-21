package com.app.application.validator.generic;

import java.util.Map;

public interface Validator<T, E> {
    Map<String, E> validate(T item);
}
