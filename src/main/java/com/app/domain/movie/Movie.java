package com.app.domain.movie;

import com.app.domain.vo.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Money price;
    private LocalDate premiereDate;

}
