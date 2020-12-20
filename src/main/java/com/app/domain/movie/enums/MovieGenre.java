package com.app.domain.movie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum MovieGenre {

    DRAMA("Drama"),
    COMEDY("Comedy"),
    THRILLER("Thriller"),
    DARK_COMEDY("Dark comedy");

    private final String desc;

    public static List<String> getAllMovieGenres() {
        return Arrays.stream(MovieGenre.values())
                .map(MovieGenre::getDesc)
                .collect(Collectors.toList());
    }
}
