package com.app.application.dto;

import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateCinemaHallDto {

    private List<Position> positions;
}
