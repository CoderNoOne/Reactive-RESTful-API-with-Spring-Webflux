package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateMailDto;
import com.app.application.exception.EmailServiceException;
import com.app.application.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EmailHandler {

    private final EmailService emailService;

    public Mono<ServerResponse> sendSingleEmail(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateMailDto.class)
                .switchIfEmpty(Mono.error(() -> new EmailServiceException("No mail info defined")))
                .flatMap(emailService::sendSingleEmail)
                .flatMap(mailDto -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(mailDto))
                );
    }
}
