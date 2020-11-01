package com.app.application.mapper;

import com.app.application.dto.CreateMovieDto;
import com.app.application.dto.CreateUserDto;
import com.app.domain.movie.Movie;
import com.app.domain.security.User;

import java.util.Optional;

public interface Mappers {


    static User fromCreateUserDtoToRegularUser(CreateUserDto createUserDto) {
        return User.builder()
                .password(createUserDto.getPassword())
                .birthDate(createUserDto.getBirthDate())
                .username(createUserDto.getUsername())
                .build();

    }

    static Movie fromCreateMovieDtoToMovie(final CreateMovieDto createMovieDto) {
        return Optional.ofNullable(createMovieDto)
                .map(value -> Movie.builder()
                        .duration(value.getDuration())
                        .genre(value.getGenre())
                        .name(value.getName())
                        .ticketPrice(value.getPrice())
                        .premiereDate(value.getPremiereDate())
                        .build())
                .orElse(null);
    }

}
