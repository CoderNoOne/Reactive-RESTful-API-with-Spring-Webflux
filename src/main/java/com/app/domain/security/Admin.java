package com.app.domain.security;

import com.app.domain.security.enums.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
@Document("admins")
public class Admin extends BaseUser {

    private Role role;

    public Admin(String username, String password) {
        super(username, password);
        this.role = Role.ROLE_ADMIN;
    }
}
