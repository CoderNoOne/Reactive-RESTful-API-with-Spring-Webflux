package com.app.domain.movie;

import com.app.application.dto.MovieDto;
import com.app.domain.generic.GenericEntity;
import com.app.domain.vo.Money;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "movies")
public class Movie implements GenericEntity {

    @Id
    private String id;

    private String name;
    private String genre;
    private Integer duration;
//    private Money ticketPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate premiereDate;

    public MovieDto toDto() {
        return MovieDto.builder()
                .id(id)
                .name(name)
                .genre(genre)
//                .price(ticketPrice)
                .premiereDate(premiereDate)
                .duration(duration)
                .build();
    }
}
