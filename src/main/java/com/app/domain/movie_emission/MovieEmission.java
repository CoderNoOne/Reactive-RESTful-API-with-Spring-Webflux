package com.app.domain.movie_emission;

import com.app.application.dto.MovieEmissionDto;
import com.app.domain.movie.Movie;
import com.app.domain.position_index.PositionIndex;
import com.app.domain.vo.Money;
import com.app.domain.vo.Position;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("movie_emissions")
public class MovieEmission {

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

    private List<PositionIndex> positionIndices;


    public List<Position> getFreePositions() {
        return positionIndices
                .stream()
                .filter(PositionIndex::isFree)
                .map(PositionIndex::getPosition)
                .collect(Collectors.toList());
    }

    public MovieEmissionDto toDto() {
        return MovieEmissionDto.builder()
                .id(id)
                .baseTicketPrice(baseTicketPrice.toString())
                .startTime(startDateTime)
                .cinemaHallId(cinemaHallId)
                .movieId(movie.getId())
                .positionIndices(positionIndices)
                .build();
    }

}
