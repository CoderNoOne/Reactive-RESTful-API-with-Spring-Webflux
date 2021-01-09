package com.app.domain.cinema_hall;

import com.app.application.dto.CinemaHallDto;
import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.vo.Position;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("cinema_halls")
public class CinemaHall {

    @Id
    private String id;

    private List<Position> positions;
    private String cinemaId;


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
}
