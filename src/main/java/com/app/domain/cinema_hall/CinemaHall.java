package com.app.domain.cinema_hall;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cinema_halls")
public class CinemaHall {

    @Id
    private String id;

    private Integer capacity;

}
