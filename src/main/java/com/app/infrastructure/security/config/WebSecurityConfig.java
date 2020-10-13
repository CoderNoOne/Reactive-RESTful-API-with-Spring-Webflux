package com.app.infrastructure.security.config;

import com.app.infrastructure.security.AuthenticationManager;
import com.app.infrastructure.security.SecurityContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Flux;


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
                .pathMatchers("/tickets/**").hasRole("USER")

                .anyExchange().authenticated()
                .and()
                .build();
    }
}
