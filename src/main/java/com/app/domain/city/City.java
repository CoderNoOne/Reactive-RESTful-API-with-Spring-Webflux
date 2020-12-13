package com.app.domain.city;

import com.app.domain.cinema.Cinema;
import com.app.domain.cinema_hall.CinemaHall;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
}
