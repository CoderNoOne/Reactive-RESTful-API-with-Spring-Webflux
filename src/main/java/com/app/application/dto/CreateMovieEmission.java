package com.app.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateMovieEmission {

    private CreateMovieDto movieDto;
//    private CinemaHallDto cinemaHallDto;

}
