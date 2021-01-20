package com.app.domain.security;

import com.app.domain.security.enums.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
@Document("users")
public final class Admin extends BaseUser {

    public Admin(String username, String password) {
        super(username, password, Role.ROLE_ADMIN);
    }
}
