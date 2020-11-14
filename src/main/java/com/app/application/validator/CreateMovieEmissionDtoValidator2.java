//package com.app.application.validator;
//
//import com.app.application.dto.CreateMovieDto;
//import com.app.application.dto.CreateMovieEmissionDto;
//import com.app.application.validator.generic.AbstractMonoValidator;
//import com.app.domain.cinema_hall.CinemaHallRepository;
//import com.app.domain.movie.MovieRepository;
//import com.app.domain.movie_emission.MovieEmissionRepository;
//import lombok.RequiredArgsConstructor;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static java.util.Objects.isNull;
//import static java.util.Objects.nonNull;
//
//@RequiredArgsConstructor
//public class CreateMovieEmissionDtoValidator2 extends AbstractMonoValidator<CreateMovieEmissionDto> {
//
//    private final MovieEmissionRepository movieEmissionRepository;
//    private final MovieRepository movieRepository;
//    private final CinemaHallRepository cinemaHallRepository;
//
//
//    @Override
//    public Mono<? extends Map<String, String>> validate(CreateMovieEmissionDto item) {
//
//        var errors = Mono.just(new HashMap<String, String>());
//
//        if (isNull(item)) {
//            return putToMapAndReturn(errors, Map.entry("dto object", "is null"));
//        }
//
//        return Mono.justOrEmpty(Optional
//                .ofNullable(item.getStartTime())
//                .map(LocalDateTime::toLocalDate))
//                .map(startTime -> startTime == null ? putToMapAndReturn(errors, Map.entry("Start time", "is null")) :
//                        Mono.zip(
//                                validateMovie(errors, item),
//                                validateCinemaHall(errors, item),
//                                (movieErrors, cinemaHallErrors) -> Stream.concat(movieErrors.entrySet().stream(), cinemaHallErrors.entrySet().stream())
//                                        .collect(Collectors.toMap(
//                                                Map.Entry::getKey,
//                                                Map.Entry::getValue)
//                                        ))
//                )
//                .flatMap(x ->
//                        Mono.zip(
//                                validateMovie(errors, item),
//                                validateCinemaHall(errors, item),
//                                (movieErrors, cinemaHallErrors) -> Stream.concat(movieErrors.entrySet().stream(), cinemaHallErrors.entrySet().stream())
//                                        .collect(Collectors.toMap(
//                                                Map.Entry::getKey,
//                                                Map.Entry::getValue)
//                                        )));)
//
//                );
//
//
//    }
//
//    private Mono<? extends Map<String, String>> validateMovie(Mono<? extends Map<String, String>> errors, CreateMovieEmissionDto item) {
//
//        return movieRepository.findById(item.getMovieId())
//                .flatMap(movie -> isNull(movie) ?
//                        putToMapAndReturn(errors, Map.entry("MovieId", "No movie with id: [%s]".formatted(item.getMovieId()))) : errors);
//    }
//
//
//    private Mono<? extends Map<String, String>> validateCinemaHall(Mono<? extends Map<String, String>> errors, CreateMovieEmissionDto item) {
//
//        return cinemaHallRepository.findById(item.getCinemaHallId())
//                .flatMap(cinemaHall -> isNull(cinemaHall) ?
//                        putToMapAndReturn(errors, Map.entry("MovieId", "No cinemaHall with id: [%s]".formatted(item.getCinemaHallId()))) : errors);
//    }
//
//}
//
