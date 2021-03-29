package com.app.application.service.enums;

import com.app.domain.movie.Movie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FrequencyByDictClass {
    GENRE(String.class),
    MOVIE(Movie.class);

    private final Class<?> clazz;


}
