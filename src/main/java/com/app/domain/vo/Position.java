package com.app.domain.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position {

    private Integer rowNo;
    private Integer colNo;
}
