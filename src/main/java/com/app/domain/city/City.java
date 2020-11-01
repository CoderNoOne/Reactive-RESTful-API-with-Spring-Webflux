package com.app.domain.city;

import com.app.domain.cinema.Cinema;
import com.app.domain.cinema_hall.CinemaHall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
