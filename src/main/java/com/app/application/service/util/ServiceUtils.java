package com.app.application.service.util;

import com.app.domain.vo.Position;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceUtils {

    public static List<Position> buildPositions(Integer rowNo, Integer colNo) {

        return IntStream.rangeClosed(1, rowNo)
                .boxed()
                .flatMap(numRow -> IntStream
                        .rangeClosed(1, colNo)
                        .boxed()
                        .map(numCol -> Position.builder()
                                .colNo(numCol)
                                .rowNo(numRow)
                                .build()))
                .collect(Collectors.toList());

    }
}
    
