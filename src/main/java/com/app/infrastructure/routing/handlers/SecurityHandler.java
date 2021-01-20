package com.app.infrastructure.routing.handlers;

import com.app.application.dto.ResponseDto;
import com.app.application.exception.AuthenticationException;
import com.app.infrastructure.aspect.annotations.Loggable;
import com.app.infrastructure.security.AppUserDetailsService;
import com.app.infrastructure.security.dto.AuthenticationDto;
import com.app.infrastructure.security.dto.TokensDto;
import com.app.infrastructure.security.tokens.AppTokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class SecurityHandler {

    private final AppUserDetailsService appUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AppTokensService appTokensService;

    @Loggable
    public Mono<ServerResponse> login(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(AuthenticationDto.class)
                .switchIfEmpty(Mono.error(() -> new AuthenticationException("Provide request body")))
                .map(dto -> {
                    if (isNull(dto.getPassword()) || isNull(dto.getUsername())) {
                        throw new AuthenticationException("Provide password and username");
                    }
                    return dto;
                })
                .flatMap(authenticationDto -> appUserDetailsService
                        .findByUsername(authenticationDto.getUsername())
                        .filter(user -> passwordEncoder.matches(authenticationDto.getPassword(), user.getPassword()))
                        .flatMap(user -> appTokensService.generateTokens(user)
                                .flatMap(tokensDto -> ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(
                                                ResponseDto.<TokensDto>builder()
                                                        .data(tokensDto)
                                                        .build()))
                                )
                        ).switchIfEmpty(Mono.error(() -> new AuthenticationException("Provide valid credentials")))

                );
    }
}
