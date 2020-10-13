package com.app.infrastructure.security;

import com.app.domain.security.UserRepository;
import com.app.infrastructure.security.tokens.AppTokensService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final AppTokensService appTokensService;
    private final UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        try {
            String authToken = authentication.getCredentials().toString();
            if (!appTokensService.isTokenValid(authToken)) {
                return Mono.error(() -> new IllegalStateException("AUTH FAILED 1"));
            }
            var userId = appTokensService.getId(authToken);
            return userRepository
                    .findById(userId)
                    .map(userFromDb -> new UsernamePasswordAuthenticationToken(
                            userFromDb.getUsername(),
                            null,
                            List.of(new SimpleGrantedAuthority(userFromDb.getRole().toString()))
                    ));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Mono.error(() -> new IllegalStateException("AUTH FAILED 2"));
        }
    }
}
