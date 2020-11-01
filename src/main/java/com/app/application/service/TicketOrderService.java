package com.app.application.service;

import com.app.application.dto.CreateTicketOrderDto;
import com.app.domain.movie_emission.MovieEmissionRepository;
import com.app.domain.security.UserRepository;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket_order.TicketOrder;
import com.app.domain.ticket_order.TicketOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketOrderService {

    private final TicketOrderRepository ticketOrderRepository;
    private final MovieEmissionRepository movieEmissionRepository;
    private final UserRepository userRepository;

    public Mono<TicketOrder> addTicketOrder(Mono<? extends Principal> principal, Mono<CreateTicketOrderDto> createTicketOrderDto) {

        return createTicketOrderDto
                .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("empty createTickerOrderDto")))
                .flatMap(value -> movieEmissionRepository
                        .findById(value.getMovieEmissionId())
                        .flatMap(movieEmission -> principal
                                .flatMap(val -> userRepository.findByUsername(val.getName()))
                                .map(user -> TicketOrder.builder()
                                        .dateOrder(LocalDate.now())
                                        .movieEmission(movieEmission)
                                        .ticketOrderType(value.getTicketOrderType())
                                        .user(user)
                                        .tickets(value.getTicketsDetails()
                                                .stream()
                                                .map(ticketDetailsDto -> Ticket.builder()
                                                        .position(ticketDetailsDto.getPosition())
                                                        .type(ticketDetailsDto.getTicketType())
                                                        .movieEmission(movieEmission)
                                                        .price(new BigDecimal("2"))/*value.getTicketOrderType()*/ // TODO: 10/31/20  )
                                                        .build())
                                                .collect(Collectors.toList()))
                                        .build()
                                )
                        ))
                .flatMap(ticketOrderRepository::addOrUpdate);
    }
}
