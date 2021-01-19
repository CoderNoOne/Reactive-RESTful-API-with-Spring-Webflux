package com.app.application.service;

import com.app.application.dto.CreateTicketPurchaseDto;
import com.app.application.exception.TicketOrderServiceException;
import com.app.application.validator.CreateTicketPurchaseDtoValidator;
import com.app.application.validator.util.Validations;
import com.app.domain.movie_emission.MovieEmissionRepository;
import com.app.domain.security.UserRepository;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.ticket_purchase.TicketPurchase;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketPurchaseService {

    private final TicketPurchaseRepository ticketPurchaseRepository;
    private final CreateTicketPurchaseDtoValidator createTicketPurchaseDtoValidator;
    private final MovieEmissionRepository movieEmissionRepository;
    private final UserRepository userRepository;

    public Mono<TicketPurchase> addTicketOrderPurchase(Mono<? extends Principal> principal, CreateTicketPurchaseDto createPurchaseDto) {

        var errors = createTicketPurchaseDtoValidator.validate(createPurchaseDto);

        if (Validations.hasErrors(errors)) {
            return Mono.error(() -> new TicketOrderServiceException("Validation errors: %s".formatted(Validations.createErrorMessage(errors))));
        }

        return Mono.just(createPurchaseDto)
                .flatMap(value -> movieEmissionRepository
                        .findById(value.getMovieEmissionId())
                        .switchIfEmpty(Mono.error(() -> new TicketOrderServiceException("No movie emission with id: %s".formatted(createPurchaseDto.getMovieEmissionId()))))
                        .map(movieEmission -> {
                            if (createPurchaseDto.areAllPositionsAvailable(movieEmission.getFreePositions())) {
                                return movieEmission;
                            }
                            throw new TicketOrderServiceException("Positions are not valid");
                        })
                        .flatMap(movieEmission -> principal
                                .flatMap(val -> userRepository.findByUsername(val.getName()))
                                .map(user -> TicketPurchase.builder()
                                        .purchaseDate(LocalDate.now())
                                        .movieEmission(movieEmission)
                                        .ticketGroupType(value.getTicketGroupType())
                                        .user(user)
                                        .tickets(value.getTicketsDetails()
                                                .stream()
                                                .map(ticketDetailsDto -> Ticket.builder()
                                                        .position(ticketDetailsDto.getPosition())
                                                        .type(ticketDetailsDto.getIndividualTicketType())
                                                        .movieEmission(movieEmission)
                                                        .ticketStatus(TicketStatus.PURCHASED)
                                                        .discount(createPurchaseDto.getBaseDiscount().add(ticketDetailsDto.getIndividualTicketType().getDiscount()))
                                                        .build())
                                                .collect(Collectors.toList()))
                                        .build()
                                )
                        ))
                .flatMap(ticketPurchaseRepository::addOrUpdate);
    }
}
