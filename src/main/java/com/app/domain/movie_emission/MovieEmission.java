package com.app.domain.movie_emission;

import com.app.domain.movie.Movie;
import com.app.domain.position_index.PositionIndex;
import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document("movie_emissions")
public class MovieEmission {

    @Id
    private String id;

    private Movie movie;
    private LocalDateTime startDateTime;

    private List<PositionIndex> positionIndices;

    public List<Position> getFreePositions() {
        return positionIndices
                .stream()
                .filter(PositionIndex::isFree)
                .map(PositionIndex::getPosition)
                .collect(Collectors.toList());
    }

}
