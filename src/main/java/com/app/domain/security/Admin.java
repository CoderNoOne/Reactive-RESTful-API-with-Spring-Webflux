package com.app.domain.security;

import com.app.application.dto.UserDto;
import com.app.domain.security.enums.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;

@NoArgsConstructor
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
@Document("users")
public final class Admin extends BaseUser {

    public Admin(String username, String password) {
        super(username, password, Role.ROLE_ADMIN);
    }

    public UserDto toUserDto() {
        return UserDto.builder()
                .id(getId())
                .username(getUsername())
                .favoriteMovies(Collections.emptyList())
                .role(getRole().name())
                .build();
    }
}
