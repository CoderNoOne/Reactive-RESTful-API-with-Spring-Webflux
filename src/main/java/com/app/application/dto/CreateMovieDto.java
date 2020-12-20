package com.app.application.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateMovieDto {

    @CsvBindByName
    private String genre;

    @CsvBindByName
    private String name;

    @CsvBindByName
    private Integer duration;

    @CsvBindByName
    private BigDecimal price;

    @CsvBindByName(format = "yyyy-MM-dd")
    private String premiereDate;
}
