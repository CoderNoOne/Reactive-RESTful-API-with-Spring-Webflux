package com.app.application.service;

import com.app.application.dto.MovieDto;
import com.app.application.exception.MovieServiceException;
import com.app.domain.movie.Movie;
import com.app.domain.movie.MovieRepository;
import com.app.domain.security.User;
import com.app.domain.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;


@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public Flux<MovieDto> getAll() {

        return movieRepository.findAll()
                .filter(Objects::nonNull)
                .map(Movie::toDto);
    }

    public Flux<MovieDto> getFilteredByKeyword(final String keyWord) {

        if (isNull(keyWord)) {
            return Flux.error(() -> new MovieServiceException("Key word is null"));
        }

        return movieRepository.findAll()
                .filter(Objects::nonNull)
                .filter(movie ->
                        Objects.equals(movie.getGenre(), keyWord) ||
                                Objects.equals(movie.getName(), keyWord) ||
                                Objects.equals(movie.getPremiereDate().toString(), keyWord) ||
                                Objects.equals(movie.getPrice().getValue().toString(), keyWord) ||
                                Objects.equals(String.valueOf(movie.getDuration()), keyWord))
                .map(Movie::toDto);

    }

    public Flux<Movie> getFilteredByGenre(final String genre) {

        if (isNull(genre)) {
            return Flux.error(() -> new MovieServiceException("Genre is null"));
        }

        return movieRepository.findAll()
                .filter(Objects::nonNull)
                .filter(movie -> genre.equals(movie.getGenre()));
    }

    public Flux<Movie> getFilteredByName(final String name) {

        if (isNull(name)) {
            return Flux.error(() -> new MovieServiceException("Name is null"));
        }

        return movieRepository.findAll()
                .filter(Objects::nonNull)
                .filter(movie -> name.equals(movie.getName()));
    }

    public Flux<Movie> getFilteredByDuration(final Integer minDuration, final Integer maxDuration) {

        var isMinDurationNull = isNull(minDuration);
        var isMaxDurationNull = isNull(maxDuration);

        if ((isMinDurationNull && isMaxDurationNull) ||
                (!isMinDurationNull && minDuration <= 0) ||
                (!isMaxDurationNull && maxDuration <= 0) ||
                (!isMinDurationNull && !isMaxDurationNull && minDuration > maxDuration)
        ) {
            return Flux.error(() -> new MovieServiceException(
                    """
                            Movie duration is not set correctly!
                                                
                            Conditions to met:
                            1) At least one boundary movie duration should be set,
                            2) Variable minDuration must not be greater than maxDuration (if defined),
                            3) Variables minDuration and maxDuration should be positive numbers (if both are defined)
                            """));
        }

        return movieRepository.findAll()
                .filter(Objects::nonNull)
                .filter(movie -> nonNull(movie.getDuration()) &&
                        (!isMinDurationNull && movie.getDuration() >= minDuration) &&
                        (!isMaxDurationNull && movie.getDuration() <= maxDuration));

    }

    public Flux<Movie> getFilteredByPremiereDate(final LocalDate minDate, final LocalDate maxDate) {

        var isMinDateNull = isNull(minDate);
        var isMaxDateNull = isNull(maxDate);

        if ((isMinDateNull && isMaxDateNull) || (!isMinDateNull && !isMaxDateNull && minDate.compareTo(maxDate) > 0)
        ) {
            return Flux.error(() -> new MovieServiceException("At least one boundary date should be defined"));
        }

        return movieRepository.findAll()
                .filter(Objects::nonNull)
                .filter(movie -> (!isMinDateNull && movie.getPremiereDate().compareTo(minDate) >= 0) &&
                        (!isMaxDateNull && movie.getPremiereDate().compareTo(maxDate) <= 0));
    }

    public Flux<Movie> getFilteredByBaseTicketPrice(final BigDecimal minPrice, final BigDecimal maxPrice) {

        var isMinPriceNull = isNull(minPrice);
        var isMaxPriceNull = isNull(maxPrice);

        if ((isMinPriceNull && isMaxPriceNull) ||
                (!isMinPriceNull && minPrice.compareTo(BigDecimal.ZERO) < 0) ||
                (!isMaxPriceNull && maxPrice.compareTo(BigDecimal.ZERO) < 0) ||
                (!isMinPriceNull && !isMaxPriceNull && minPrice.compareTo(maxPrice) > 0)
        ) {
            return Flux.error(() -> new MovieServiceException("At least one boundary price should be defined, minPrice must not be greater than maxPrice (if defined) and both prizes should be positive"));
        }

        return movieRepository.findAll()
                .filter(movie -> nonNull(movie) && nonNull(movie.getPrice()) && nonNull(movie.getPrice().getValue()))
                .filter(movie -> (!isMinPriceNull && movie.getPrice().getValue().compareTo(minPrice) >= 0) &&
                        (!isMaxPriceNull && movie.getPrice().getValue().compareTo(maxPrice) <= 0));
    }

    public Mono<Movie> addMovieToFavorites(final String movieId, final String username) {

        return movieRepository.findById(movieId)
                .flatMap(movie -> userRepository
                        .findByUsername(username)
                        .map(user -> {
                            user.getFavoriteMovies().add(movie);
                            return movie;
                        })
                );
    }
}
