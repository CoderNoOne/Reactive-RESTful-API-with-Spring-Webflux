package com.app.application.dto;

import lombok.*;
import org.springframework.core.NestedRuntimeException;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponseDto extends NestedRuntimeException {

    public ExceptionResponseDto(String msg) {
        super(msg);
    }
}
