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
import com.app.domain.ticket.TicketRepository;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.ticket_order.TicketOrder;
import com.app.domain.ticket_order.TicketOrderRepository;
import com.app.domain.ticket_order.enums.TicketOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
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
    private final TicketRepository ticketRepository;
    private final CreateTicketsOrderDtoValidator createTicketsOrderDtoValidator;
    private final TransactionalOperator transactionalOperator;


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
                            if (createTicketOrderDto.areAllPositionsAvailable(movieEmission.getFreePositions())) {
                                return movieEmission;
                            }
                            throw new TicketOrderServiceException("Positions are not valid");
                        })
                        .flatMap(movieEmission ->  movieEmissionRepository.addOrUpdate(movieEmission.removeFreePositions(createTicketOrderDto.getTicketsDetails()))
                                .then(principal)
                                .flatMap(val -> userRepository.findByUsername(val.getName()))
                                .map(user -> TicketOrder.builder()
                                        .orderDate(LocalDate.now())
                                        .movieEmission(movieEmission)
                                        .ticketGroupType(value.getTicketGroupType())
                                        .ticketOrderStatus(TicketOrderStatus.ORDERED)
                                        .user(user)
                                        .tickets(value.getTicketsDetails()
                                                .stream()
                                                .map(ticketDetailsDto -> Ticket.builder()
                                                        .position(ticketDetailsDto.getPosition())
                                                        .type(ticketDetailsDto.getIndividualTicketType())
                                                        .ticketStatus(TicketStatus.PURCHASED)
                                                        .discount(createTicketOrderDto.getBaseDiscount().add(ticketDetailsDto.getIndividualTicketType().getDiscount()))
                                                        .build())
                                                .collect(Collectors.toList()))
                                        .build())))
                .flatMap(ticketOrder -> ticketRepository.addOrUpdateMany(ticketOrder.getTickets())
                        .then(ticketOrderRepository.addOrUpdate(ticketOrder))
                        .map(TicketOrder::toDto))
                .as(transactionalOperator::transactional);

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

    public Flux<TicketOrderDto> getAllTicketOrdersForLoggedUser(String username) {

        return ticketOrderRepository.findAllByUsername(username)
                .map(TicketOrder::toDto);
    }

}
