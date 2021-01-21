package com.app.infrastructure.routing.handlers;

import com.app.application.dto.CreateMailDto;
import com.app.application.dto.CreateMailsDto;
import com.app.application.dto.MailDto;
import com.app.application.dto.ResponseDto;
import com.app.application.exception.EmailServiceException;
import com.app.application.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
                        .body(BodyInserters.fromValue(ResponseDto.<MailDto>builder().data(mailDto).build()))
                );
    }

    public Mono<ServerResponse> sendMultipleEmails(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(CreateMailsDto.class)
                .switchIfEmpty(Mono.error(() -> new EmailServiceException("No mails info defined")))
                .map(emailService::sendMultipleEmails)
                .flatMap(Flux::collectList)
                .flatMap(list -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(
                                ResponseDto.<List<MailDto>>builder()
                                        .data(list)
                                        .build())
                        ));
    }
}
