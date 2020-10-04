package com.app.domain.position_index;

import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("position_indices")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PositionIndex {

    @Id
    private String id;

    private Position position;
    private boolean isFree;
}
