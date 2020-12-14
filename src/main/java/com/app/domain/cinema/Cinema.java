package com.app.domain.cinema;

import com.app.application.dto.CinemaDto;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.city.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document("cinemas")
public class Cinema {

    @Id
    private String id;

    private String city;
    private String street;

    private List<CinemaHall> cinemaHalls;

    public CinemaDto toDto(){

        return CinemaDto.builder()
                .id(id)
                .city(city)
                .street(street)
                .hallsCapacity(cinemaHalls
                        .stream()
                        .collect(Collectors.toMap(
                                CinemaHall::getId,
                                e -> e.getPositions().size()
                        ))
                )
                .build();
    }
}
