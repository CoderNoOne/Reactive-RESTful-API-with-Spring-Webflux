package com.app.domain.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public abstract class BaseUser {

    @Id
    private String id;
    private String username;
    private String password;

    public BaseUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
