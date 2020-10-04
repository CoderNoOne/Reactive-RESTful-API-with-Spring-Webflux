package com.app.application.dto;

import com.app.domain.vo.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateMovieDto {

    private String name;
    private String genre;
    private Integer duration;
    private Money price;
    private LocalDate premiereDate;
}
