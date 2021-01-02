package com.app.application.service;

import com.app.application.dto.CreateMovieEmissionDto;
import com.app.application.dto.MovieEmissionDto;
import com.app.application.exception.MovieEmissionServiceException;
import com.app.application.validator.CreateMovieEmissionDtoValidator;
import com.app.application.validator.util.Validations;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.movie.Movie;
import com.app.domain.movie.MovieRepository;
import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.movie_emission.MovieEmissionMapper;
import com.app.domain.movie_emission.MovieEmissionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieEmissionService {

    private final static Integer MINIMAL_BREAK_BETWEEN_MOVIE_EMISSIONS_IN_MIN = 10;

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
                                if (movie.getPremiereDate().compareTo(toLocalDateTime(item.getStartTime()).toLocalDate()) > 0) {
                                    return Mono.error(() -> new MovieEmissionServiceException("Movie with id: %s cannot be displayed in %s - before premiere date: %s".formatted(item.getMovieId(), item.getStartTime(), movie.getPremiereDate())));
                                }
                                return Mono.just(movie);
                            })
                            .flatMap(movie -> cinemaHallRepository.findById(item.getCinemaHallId())
                                    .switchIfEmpty(Mono.error(() -> new MovieEmissionServiceException("No cinema hall with id: [%s]".formatted(item.getCinemaHallId()))))
                                    .map(cinemaHall -> {
                                        if (!isFreeSpaceForMovieEmissionInCinemaHall(cinemaHall, movie, item.getStartTime())) {
                                            throw new MovieEmissionServiceException("No time space for this movieEmission in this cinema hall!");
                                        }
                                        return cinemaHall;
                                    })
                                    .flatMap(cinemaHall -> movieEmissionRepository.addOrUpdate(
                                            MovieEmission.builder()
                                                    .movie(movie)
                                                    .startDateTime(toLocalDateTime(item.getStartTime()))
                                                    .cinemaHall(cinemaHall)
                                                    .build()))
                                    .map(MovieEmissionMapper::mapMovieEmissionToDto)
                            );
                });

    }

    private boolean isFreeSpaceForMovieEmissionInCinemaHall(CinemaHall cinemaHall, Movie movie, String startDateTimeOfMovieEmission) {

        var startDateTime = toLocalDateTime(startDateTimeOfMovieEmission);
        var movieEmissionTimesInDay = getMovieEmissionTimesInDay(startDateTime.toLocalDate(), cinemaHall);

        var counter = new AtomicInteger(1);

        var bookedSpaceForMovieEmissions = movieEmissionTimesInDay
                .stream()
                .map(pair -> counter.get() != 0 && counter.get() != movieEmissionTimesInDay.size() ? pair : Pair.of(pair.getLeft().plusMinutes(10), pair.getRight().plusMinutes(10)))
                .peek((pair) -> counter.incrementAndGet())
                .collect(Collectors.toList());

        var to = startDateTime.plusHours(movie.getDuration()).toLocalTime();
        var from = startDateTime.toLocalTime();
        


        return false;
    }

    private List<Pair<LocalTime, LocalTime>> getMovieEmissionTimesInDay(LocalDate date, CinemaHall cinemaHall) {

        return cinemaHall.getMovieEmissions()
                .stream()
                .filter(movieEmission -> movieEmission.getStartDateTime().toLocalDate().compareTo(date) == 0)
                .sorted(Comparator.comparing(MovieEmission::getStartDateTime))
                .map(movieEmission -> Pair.of(LocalTime.from(movieEmission.getStartDateTime()), LocalTime.from(movieEmission.getStartDateTime().plusHours(movieEmission.getMovie().getDuration()))))
                .collect(Collectors.toList());
    }

    private LocalDateTime toLocalDateTime(String stringValue) {
        return LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse(stringValue));
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
