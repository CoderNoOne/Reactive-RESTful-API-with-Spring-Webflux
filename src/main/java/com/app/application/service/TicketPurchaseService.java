package com.app.application.service;

import com.app.application.dto.CreateTicketPurchaseDto;
import com.app.application.exception.TicketOrderServiceException;
import com.app.application.exception.TicketPurchaseServiceException;
import com.app.application.validator.CreateTicketPurchaseDtoValidator;
import com.app.application.validator.util.Validations;
import com.app.domain.movie_emission.MovieEmissionRepository;
import com.app.domain.security.UserRepository;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.ticket_order.TicketOrder;
import com.app.domain.ticket_order.TicketOrderRepository;
import com.app.domain.ticket_purchase.TicketPurchase;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TicketPurchaseService {

    private final TicketPurchaseRepository ticketPurchaseRepository;
    private final CreateTicketPurchaseDtoValidator createTicketPurchaseDtoValidator;
    private final MovieEmissionRepository movieEmissionRepository;
    private final UserRepository userRepository;
    private final TicketOrderRepository ticketOrderRepository;
    private final TransactionalOperator transactionalOperator;

    public Mono<TicketPurchase> purchaseTicket(Mono<? extends Principal> principal, CreateTicketPurchaseDto createPurchaseDto) {

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

    public Mono<TicketPurchase> purchaseTicketFromOrder(String username, String ticketOrderId) {

        if (isNull(ticketOrderId)) {
            throw new TicketPurchaseServiceException("Ticket order id is null");
        }

        return ticketOrderRepository.findById(ticketOrderId)
                .map(ticketOrder -> getIfTicketOrderIsUsable(ticketOrder, username))
                .flatMap(ticketOrder ->
                        ticketOrderRepository.addOrUpdate(ticketOrder.changeOrderStatusToDone())
                                .then(ticketPurchaseRepository.addOrUpdate(ticketOrder.toTicketPurchase())))
                .as(transactionalOperator::transactional);

    }

    private TicketOrder getIfTicketOrderIsUsable(TicketOrder ticketOrder, String username) {
        if (!Objects.equals(username, ticketOrder.getUser().getUsername())) {
            throw new TicketPurchaseServiceException("Ticket order is not done by you!");
        } else if (ticketOrder.getMovieEmission().getStartDateTime().toLocalDate().compareTo(LocalDate.now().minusDays(1)) < 0) {
            throw new TicketPurchaseServiceException("You cannot purchase ticket 1 day before emission");
        }

        return ticketOrder;
    }
}
