package com.app.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {

    private String id;
    private String username;
    private String birthDate;
    private String role;
    private List<MovieDto> favoriteMovies;

}
