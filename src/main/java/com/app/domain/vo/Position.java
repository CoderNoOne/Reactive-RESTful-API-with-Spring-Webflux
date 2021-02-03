package com.app.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position {

    private Integer rowNo;
    private Integer colNo;

    public Position(String value) {

        String[] array = value.split(", ");

        if (array.length == 2) {
            rowNo = Integer.parseInt(array[0].split(": ")[1]);
            colNo = Integer.parseInt(array[1].split(": ")[1]);
        }
    }

    @Override
    public String toString() {
        return "rowNo: " + rowNo + ", colNo: " + colNo;
    }
}
