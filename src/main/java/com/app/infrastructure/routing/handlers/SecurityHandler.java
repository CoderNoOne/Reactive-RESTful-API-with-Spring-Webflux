package com.app.infrastructure.routing.handlers;

import com.app.application.dto.ResponseDto;
import com.app.infrastructure.aspect.annotations.Loggable;
import com.app.infrastructure.security.AppUserDetailsService;
import com.app.infrastructure.security.dto.AuthenticationDto;
import com.app.infrastructure.security.dto.TokensDto;
import com.app.infrastructure.security.tokens.AppTokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityHandler {

    private final AppUserDetailsService appUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AppTokensService appTokensService;

    @Loggable
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        Mono<AuthenticationDto> authenticationDtoMono = serverRequest.bodyToMono(AuthenticationDto.class);
        return authenticationDtoMono
                .flatMap(authenticationDto -> appUserDetailsService
                        .findByUsername(authenticationDto.getUsername())
                        .filter(user -> passwordEncoder.matches(authenticationDto.getPassword(), user.getPassword()))
                        .flatMap(user -> appTokensService.generateTokens(user)
                                .flatMap(tokensDto -> ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(
                                                ResponseDto.<TokensDto>builder()
                                                        .data(tokensDto)
                                                        .build()))
                                )
                        ).switchIfEmpty(Mono.error(() -> new IllegalArgumentException("asdsad")))

                );
    }
}
