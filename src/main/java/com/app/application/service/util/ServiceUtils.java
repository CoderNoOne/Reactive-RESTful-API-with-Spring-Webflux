package com.app.application.service.util;

import com.app.domain.vo.Position;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceUtils {

    public static List<Position> buildPositions(Integer positionNumber) {
        int sqrt = (int) Math.sqrt(positionNumber);
        var positions = new ArrayList<Position>();

        int rest = positionNumber - sqrt * sqrt;

        int i1 = rest / sqrt; /*tyle dodatkowych  pełnych rzędów*/

        int i2 = rest % sqrt;/*tyle miejsc zajętych w ostanim rzedzie*/

        for (int i = 1; i <= sqrt; i++) {
            for (int j = 1; j <= sqrt + i1; j++) {
                positions.add(Position.builder()
                        .rowNo(j)
                        .colNo(i)
                        .build());
            }
        }

        for (int i = 1; i <= i2; i++) {
            positions.add(Position.builder()
                    .rowNo(sqrt + i1 + 1)
                    .colNo(i)
                    .build());

        }

        return positions;

    }
}
