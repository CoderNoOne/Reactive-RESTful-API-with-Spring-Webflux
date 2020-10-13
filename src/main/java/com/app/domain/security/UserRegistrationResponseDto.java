package com.app.domain.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRegistrationResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;
}
