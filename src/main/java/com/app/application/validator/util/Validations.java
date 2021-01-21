package com.app.application.validator.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Validations {

    static String createErrorMessage(Map<String, ?> errors) {
        return errors
                .entrySet()
                .stream()
                .map(e -> {
                    if (e.getValue() instanceof String value) {
                        return "%s -> %s".formatted(e.getKey(), value);
                    }
                    if (e.getValue() instanceof List<?> list) {
                        return "Item no. %s | %s".formatted(e.getKey(), list.stream()
                                .map(item -> item instanceof Map.Entry<?, ?> entry ? entry : null)
                                .filter(Objects::nonNull)
                                .map(item -> "%s -> %s".formatted(item.getKey(), item.getValue()))
                                .collect(Collectors.joining(", ")));
                    }
                    return "";

                })
                .collect(Collectors.joining(", "));
    }

    static boolean hasErrors(Map<String, ?> errors) {
        return !errors.isEmpty();
    }

}
