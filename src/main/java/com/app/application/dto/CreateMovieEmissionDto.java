package com.app.application.dto;

import com.app.domain.vo.Money;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateMovieEmissionDto {

    private String movieId;
    private String cinemaHallId;
    private String startTime;
    private String baseTicketPrice;
}
