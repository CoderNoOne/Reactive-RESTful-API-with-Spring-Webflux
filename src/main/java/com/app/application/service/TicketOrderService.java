package com.app.application.service;

import com.app.application.dto.CreateTicketOrderDto;
import com.app.application.dto.TicketDetailsDto;
import com.app.application.dto.TicketOrderDto;
import com.app.application.exception.TicketOrderServiceException;
import com.app.application.validator.CreateTicketsOrderDtoValidator;
import com.app.application.validator.util.Validations;
import com.app.domain.movie_emission.MovieEmissionRepository;
import com.app.domain.security.UserRepository;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.ticket_order.TicketOrder;
import com.app.domain.ticket_order.TicketOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TicketOrderService {

    private final TicketOrderRepository ticketOrderRepository;
    private final MovieEmissionRepository movieEmissionRepository;
    private final UserRepository userRepository;
    private final CreateTicketsOrderDtoValidator createTicketsOrderDtoValidator;


    public Mono<TicketOrderDto> addTicketOrder(Mono<? extends Principal> principal, CreateTicketOrderDto createTicketOrderDto) {

        var errors = createTicketsOrderDtoValidator.validate(createTicketOrderDto);

        if (Validations.hasErrors(errors)) {
            return Mono.error(() -> new TicketOrderServiceException("Validation errors: %s".formatted(Validations.createErrorMessage(errors))));
        }

        return Mono.justOrEmpty(createTicketOrderDto)
                .switchIfEmpty(Mono.error(() -> new TicketOrderServiceException("empty createTickerOrderDto")))
                .flatMap(value -> movieEmissionRepository
                        .findById(value.getMovieEmissionId())
                        .switchIfEmpty(Mono.error(() -> new TicketOrderServiceException("No movie emission with id: %s".formatted(createTicketOrderDto.getMovieEmissionId()))))
                        .map(movieEmission -> {
                            if (createTicketOrderDto.getTicketsDetails().stream()
                                    .map(TicketDetailsDto::getPosition)
                                    .allMatch(position -> movieEmission.getFreePositions().contains(position))
                            ) {
                                return movieEmission;
                            }
                            throw new TicketOrderServiceException("Positions are not valid");
                        })
                        .flatMap(movieEmission -> principal
                                .flatMap(val -> userRepository.findByUsername(val.getName()))
                                .map(user -> TicketOrder.builder()
                                        .orderDate(LocalDate.now())
                                        .movieEmission(movieEmission)
                                        .ticketGroupType(value.getTicketGroupType())
                                        .user(user)
                                        .tickets(value.getTicketsDetails()
                                                .stream()
                                                .map(ticketDetailsDto -> Ticket.builder()
                                                        .position(ticketDetailsDto.getPosition())
                                                        .type(ticketDetailsDto.getIndividualTicketType())
                                                        .movieEmission(movieEmission)
                                                        .discount(createTicketOrderDto.getBaseDiscount())
                                                        .ticketStatus(TicketStatus.ORDERED)
                                                        .build())
                                                .collect(Collectors.toList()))
                                        .build()
                                )
                        ))
                .flatMap(ticketOrderRepository::addOrUpdate)
                .map(TicketOrder::toDto);
    }


    public Mono<TicketOrderDto> cancelOrder(String username, String orderId) {

        if (isNull(orderId)) {
            throw new TicketOrderServiceException("Order id is null");
        }

        return ticketOrderRepository.findById(orderId)
                .map(ticketOrder -> {
                    if (!Objects.equals(ticketOrder.getUser().getUsername(), username)) {
                        throw new TicketOrderServiceException("That ticker order does not belong to you");
                    }
                    return ticketOrder.changeOrderStatusToCancelled();
                })
                .map(TicketOrder::toDto);
    }
}
