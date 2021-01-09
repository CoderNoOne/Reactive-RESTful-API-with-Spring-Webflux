package com.app.application.dto;

import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CinemaHallDto {

    private String id;
    private String cinemaId;
    private List<Position> positions;

    private List<MovieEmissionDto> movieEmissions;

}
