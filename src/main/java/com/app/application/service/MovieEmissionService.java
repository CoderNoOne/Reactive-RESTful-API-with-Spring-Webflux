package com.app.application.service;

import com.app.application.dto.CreateMovieEmissionDto;
import com.app.application.dto.MovieEmissionDto;
import com.app.application.exception.MovieEmissionServiceException;
import com.app.application.validator.CreateMovieEmissionDtoValidator;
import com.app.application.validator.util.Validations;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.movie.MovieRepository;
import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.movie_emission.MovieEmissionMapper;
import com.app.domain.movie_emission.MovieEmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieEmissionService {

    private final MovieEmissionRepository movieEmissionRepository;
    private final CinemaHallRepository cinemaHallRepository;
    private final MovieRepository movieRepository;

    public Mono<MovieEmissionDto> createMovieEmission(Mono<CreateMovieEmissionDto> createMovieEmission) {

        return createMovieEmission
                .flatMap(item -> {

                    var createMovieEmissionDtoValidator = new CreateMovieEmissionDtoValidator();
                    var errors = createMovieEmissionDtoValidator.validate(item);

                    if (Validations.hasErrors(errors)) {
                        return Mono.error(() -> new MovieEmissionServiceException(Validations.createErrorMessage(errors)));
                    }

                    return movieRepository
                            .findById(item.getMovieId())
                            .switchIfEmpty(Mono.error(() -> new MovieEmissionServiceException("No movie with id: [%s]".formatted(item.getMovieId()))))
                            .flatMap(movie -> {
                                if (movie.getPremiereDate().compareTo(item.getStartTime().toLocalDate()) > 0) {
                                    return Mono.error(() -> new MovieEmissionServiceException(""));
                                }
                                return Mono.just(movie);
                            })
                            .flatMap(movie -> cinemaHallRepository.findById(item.getCinemaHallId())
                                    .switchIfEmpty(Mono.error(() -> new MovieEmissionServiceException("No cinema hall with id: [%s]".formatted(item.getCinemaHallId()))))
                                    .flatMap(cinemaHall -> movieEmissionRepository.addOrUpdate(
                                            MovieEmission.builder()
                                                    .movie(movie)
                                                    .startDateTime(item.getStartTime())
                                                    .cinemaHall(cinemaHall)
                                                    .build()))
                                    .map(MovieEmissionMapper::mapMovieEmissionToDto)
                            );
                });

    }

    public Flux<MovieEmissionDto> getAllMovieEmissions() {

        return movieEmissionRepository
                .findAll()
                .map(MovieEmissionMapper::mapMovieEmissionToDto);
    }

    public Flux<MovieEmissionDto> getAllMovieEmissionsByMovieId(String movieId) {

        return movieEmissionRepository
                .findMovieEmissionsByMovieId(movieId)
                .map(MovieEmissionMapper::mapMovieEmissionToDto);
    }


}
