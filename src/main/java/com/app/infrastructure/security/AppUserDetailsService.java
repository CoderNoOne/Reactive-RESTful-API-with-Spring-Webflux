package com.app.infrastructure.security;

import com.app.application.exception.AuthenticationException;
import com.app.domain.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService {
    protected final UserRepository userRepository;

    public Mono<User> findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        true, true, true, true,
                        List.of(new SimpleGrantedAuthority(user.getRole().toString()))
                ));
    }
}
