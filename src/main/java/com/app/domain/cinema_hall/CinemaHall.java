package com.app.domain.cinema_hall;

import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("cinema_halls")
public class CinemaHall {

    @Id
    private String id;

    private Set<Position> positions;

    private List<MovieEmission> movieEmissions;

}
