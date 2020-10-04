package com.app.application.mapper;

import com.app.application.dto.CreateUserDto;
import com.app.domain.security.User;

public interface Mappers {


    static User fromCreateUserDtoToRegularUser(CreateUserDto createUserDto) {
        return User.builder()
                .password(createUserDto.getPassword())
                .birthDate(createUserDto.getBirthDate())
                .username(createUserDto.getUsername())
                .build();



    }

}
