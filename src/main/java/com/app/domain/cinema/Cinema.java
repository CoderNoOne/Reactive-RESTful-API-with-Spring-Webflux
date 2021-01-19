package com.app.domain.cinema;

import com.app.application.dto.CinemaDto;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.city.City;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("cinemas")
public class Cinema {

    @Id
    @Getter
    private String id;

    private String city;
    private String street;

    @Getter
    private List<CinemaHall> cinemaHalls;

    public Cinema setCity(String city) {
        this.city = city;
        return this;
    }

    public Cinema setCinemasIdForCinemaHalls(String cinemaId) {

        if (nonNull(cinemaHalls)) {
            cinemaHalls.forEach(cinemaHall -> cinemaHall.setCinemaId(cinemaId));
        }
        return this;
    }

    public CinemaDto toDto() {

        return CinemaDto.builder()
                .id(id)
                .city(city)
                .street(street)
                .hallsCapacity(cinemaHalls
                        .stream()
                        .collect(Collectors.toMap(
                                CinemaHall::getId,
                                e -> e.getPositions().size())))
                .build();
    }
}
