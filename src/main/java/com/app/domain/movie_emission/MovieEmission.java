package com.app.domain.movie_emission;

import com.app.application.dto.MovieEmissionDto;
import com.app.application.dto.TicketDetailsDto;
import com.app.domain.generic.GenericEntity;
import com.app.domain.movie.Movie;
import com.app.domain.position_index.PositionIndex;
import com.app.domain.vo.Money;
import com.app.domain.vo.Position;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("movie_emissions")
public class MovieEmission implements GenericEntity {

    @Id
    @Getter
    private String id;

    @Getter
    private Movie movie;

    @Getter
    private LocalDateTime startDateTime;

    @Getter
    private Money baseTicketPrice;

    @Getter
    private String cinemaHallId;

    @Getter
    private Map<Position, Boolean> isPositionFree;


    public List<Position> getFreePositions() {

        return isPositionFree
                .entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .collect(ArrayList::new, (list, entry) -> list.add(entry.getKey()), ArrayList::addAll);

    }

    public MovieEmissionDto toDto() {
        return MovieEmissionDto.builder()
                .id(id)
                .movieId(movie.getId())
                .startTime(startDateTime)
                .cinemaHallId(cinemaHallId)
                .isPositionFree(isPositionFree)
                .baseTicketPrice(baseTicketPrice.getValue().toString())
                .build();
    }

    public MovieEmission removeFreePositions(List<TicketDetailsDto> ticketsDetails) {

        Optional
                .ofNullable(ticketsDetails)
                .map(Collection::stream)
                .map(stream -> stream.map(TicketDetailsDto::getPosition))
                .ifPresent(stream -> stream
                        .forEach(position -> isPositionFree.computeIfPresent(position, (key, value) -> false)));

        return this;
    }
}

