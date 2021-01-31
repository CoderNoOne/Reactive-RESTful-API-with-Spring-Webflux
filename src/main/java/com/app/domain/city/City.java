package com.app.domain.city;

import com.app.application.dto.CinemaInCityDto;
import com.app.application.dto.CityDto;
import com.app.domain.cinema.Cinema;
import com.app.domain.cinema_hall.CinemaHall;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("cities")
public class City {

    @Id
    private String id;

    @Getter
    private String name;
    @Getter
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

    public City addCinema(Cinema cinema) {
        if (isNull(cinemas)) {
            cinemas = new ArrayList<>(Collections.singletonList(cinema));
        } else {
            cinemas.add(cinema);
        }
        return this;
    }
}
