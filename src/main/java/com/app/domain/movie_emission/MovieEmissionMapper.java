package com.app.domain.movie_emission;

import com.app.application.dto.MovieEmissionDto;

public interface MovieEmissionMapper {

    static MovieEmissionDto mapMovieEmissionToDto(MovieEmission movieEmission) {
        return MovieEmissionDto.builder()
                .id(movieEmission.getId())
                .movieId(movieEmission.getMovie().getId())
                .startTime(movieEmission.getStartDateTime())
                .cinemaHallId(movieEmission.getCinemaHall().getId())
                .build();
    }
}
