package com.app.application.dto;

import com.app.domain.position_index.PositionIndex;
import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MovieEmissionDto {

    private String id;
    private String movieId;
    private LocalDateTime startTime;
    private String cinemaHallId;
    private Map<Position, Boolean> isPositionFree;
//    private List<PositionIndex> positionIndices;
    private String baseTicketPrice;
}
