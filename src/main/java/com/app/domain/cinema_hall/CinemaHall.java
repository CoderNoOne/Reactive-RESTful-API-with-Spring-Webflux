package com.app.domain.cinema_hall;

import com.app.application.dto.CinemaHallDto;
import com.app.domain.generic.GenericEntity;
import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.vo.Position;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("cinema_halls")
public class CinemaHall implements GenericEntity {

    @Id
    @Getter
    private String id;

    @Getter
    private List<Position> positions;

    @Setter
    @Getter
    private String cinemaId;

    @Getter
    private List<MovieEmission> movieEmissions;

    public CinemaHallDto toDto() {
        return CinemaHallDto.builder()
                .id(id)
                .cinemaId(cinemaId)
                .movieEmissions(movieEmissions
                        .stream()
                        .map(MovieEmission::toDto)
                        .collect(Collectors.toList())
                )
                .rowNo(getMaxNumber(Position::getRowNo))
                .colNo(getMaxNumber(Position::getColNo))
                .build();
    }

    private Integer getMaxNumber(Function<Position, Integer> function) {
        return positions
                .stream()
                .map(function)
                .reduce(1, (pos1, pos2) -> pos1 > pos2 ? pos1 : pos2);
    }

    public CinemaHall addMovieEmission(MovieEmission movieEmission) {
        if (nonNull(movieEmissions)) {
            movieEmissions.add(movieEmission);
        } else {
            movieEmissions = new ArrayList<>(Collections.singletonList(movieEmission));
        }
        return this;
    }

    public CinemaHall removeMovieEmissionById(String movieEmissionId) {
        if (nonNull(movieEmissions)) {
            movieEmissions.removeIf(movieEmission -> movieEmission.getId().equals(movieEmissionId));
        }
        return this;
    }

}
