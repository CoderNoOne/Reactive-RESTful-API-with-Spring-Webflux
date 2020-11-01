package com.app.domain.movie;

import com.app.application.dto.MovieDto;
import com.app.domain.vo.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "movies")
public class Movie {

    @Id
    private String id;

    private String name;
    private String genre;
    private Integer duration;
    private Money ticketPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate premiereDate;

    public MovieDto toDto() {
        return MovieDto.builder()
                .id(id)
                .name(name)
                .genre(genre)
                .price(ticketPrice)
                .premiereDate(premiereDate)
                .duration(duration)
                .build();
    }
}
