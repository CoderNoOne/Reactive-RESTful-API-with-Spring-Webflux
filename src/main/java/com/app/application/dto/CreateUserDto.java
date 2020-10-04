package com.app.application.dto;

import com.app.domain.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {
    private String username;
    private String password;
    private String passwordConfirmation;
    private Role role;
    private LocalDate birthDate;
}