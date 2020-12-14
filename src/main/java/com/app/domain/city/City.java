package com.app.domain.city;

import com.app.application.dto.CinemaInCityDto;
import com.app.application.dto.CityDto;
import com.app.domain.cinema.Cinema;
import com.app.domain.cinema_hall.CinemaHall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document("cities")
public class City {

    @Id
    private String id;

    private String name;
    private List<Cinema> cinemas;

    public CityDto toDto() {

        return CityDto.builder()
                .id(id)
                .name(name)
                .cinemas(cinemas.stream()
                        .map(cinema -> CinemaInCityDto.builder()
                                .id(cinema.getId())
                                .cinemaHallsCapacities(cinema.getCinemaHalls()
                                .stream()
                                .collect(Collectors.toMap(
                                        CinemaHall::getId,
                                        e -> e.getPositions().size()
                                ))).build()
                        ).collect(Collectors.toList()))
                .build();
    }
}
