package com.app.infrastructure.security.config;

import com.app.infrastructure.security.AuthenticationManager;
import com.app.infrastructure.security.SecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Autowired
    private DataBufferFactory dataBufferFactory;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ServerAuthenticationEntryPoint serverAuthenticationEntryPoint() {
        return (serverWebExchange, e) -> {
            DataBuffer dataBuffer = dataBufferFactory.wrap(e.getMessage().getBytes());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return serverWebExchange.getResponse().writeWith(Flux.just(dataBuffer));
        };
    }

    @Bean
    public ServerAccessDeniedHandler serverAccessDeniedHandler() {
        return (serverWebExchange, e) -> {
            DataBuffer dataBuffer = dataBufferFactory.wrap(e.getMessage().getBytes());
            serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return serverWebExchange.getResponse().writeWith(Flux.just(dataBuffer));
        };
    }

    @Bean
    public ServerAuthenticationFailureHandler serverAuthenticationFailureHandler() {
        return new ServerAuthenticationFailureHandler() {
            @Override
            public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
                webFilterExchange.getExchange().getResponse().setRawStatusCode(HttpStatus.UNAUTHORIZED.value());

                ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
                return Mono.empty();
            }
        };
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setAuthenticationFailureHandler(serverAuthenticationFailureHandler());
        return authenticationWebFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http

                .csrf()
                .disable()

                .formLogin()
                .disable()

                .httpBasic()
                .disable()

                .authenticationManager(authenticationManager)

                .securityContextRepository(securityContextRepository)

                .exceptionHandling()
                .authenticationEntryPoint(serverAuthenticationEntryPoint())
                .accessDeniedHandler(serverAccessDeniedHandler())

                .and()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/login").permitAll()
                .pathMatchers("/security/register").permitAll()
                .pathMatchers(HttpMethod.POST, "/movies").hasRole("ADMIN")
                .pathMatchers("/movies/**").hasAnyRole("USER", "ADMIN")
                .pathMatchers("/tickets/**").hasRole("USER")
                .pathMatchers("/ticketOrders/**").hasRole("USER")

                .anyExchange().authenticated()
                .and().addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
