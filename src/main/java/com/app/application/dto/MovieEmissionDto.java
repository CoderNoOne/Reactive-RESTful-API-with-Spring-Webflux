package com.app.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MovieEmissionDto {

    private String id;
    private String movieId;
    private LocalDateTime startTime;
    private String cinemaHallId;
}
