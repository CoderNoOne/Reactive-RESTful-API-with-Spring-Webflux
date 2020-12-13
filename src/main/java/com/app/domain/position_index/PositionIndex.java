package com.app.domain.position_index;

import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PositionIndex {

    private Position position;
    private boolean isFree;
}
