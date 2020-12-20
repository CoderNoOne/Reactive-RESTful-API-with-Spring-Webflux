package com.app.application.validator.util;

import java.util.Map;
import java.util.stream.Collectors;

public interface Validations {

    static String createErrorMessage(Map<String, String> errors) {
        return errors
                .entrySet()
                .stream()
                .map(e -> "%s %s".formatted(e.getKey(), e.getValue()))
                .collect(Collectors.joining(", "));
    }

    static boolean hasErrors(Map<String, String> errors) {
        return !errors.isEmpty();
    }
}
