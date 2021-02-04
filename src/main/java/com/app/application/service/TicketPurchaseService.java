package com.app.application.service;

import com.app.application.dto.CreateTicketPurchaseDto;
import com.app.application.dto.TicketPurchaseDto;
import com.app.application.exception.TicketOrderServiceException;
import com.app.application.exception.TicketPurchaseServiceException;
import com.app.application.validator.CreateTicketPurchaseDtoValidator;
import com.app.application.validator.util.Validations;
import com.app.domain.cinema.Cinema;
import com.app.domain.cinema.CinemaRepository;
import com.app.domain.cinema_hall.CinemaHall;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.city.City;
import com.app.domain.city.CityRepository;
import com.app.domain.movie_emission.MovieEmissionRepository;
import com.app.domain.security.UserRepository;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket.TicketRepository;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.ticket_order.TicketOrder;
import com.app.domain.ticket_order.TicketOrderRepository;
import com.app.domain.ticket_purchase.TicketPurchase;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import com.sun.mail.imap.IMAPBodyPart;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TicketPurchaseService {

    private final TicketPurchaseRepository ticketPurchaseRepository;
    private final CreateTicketPurchaseDtoValidator createTicketPurchaseDtoValidator;
    private final MovieEmissionRepository movieEmissionRepository;
    private final UserRepository userRepository;
    private final CinemaHallRepository cinemaHallRepository;
    private final TicketRepository ticketRepository;
    private final CityRepository cityRepository;
    private final TicketOrderRepository ticketOrderRepository;
    private final TransactionalOperator transactionalOperator;

    public Mono<TicketPurchaseDto> purchaseTicket(Mono<? extends Principal> principal, CreateTicketPurchaseDto createPurchaseDto) {

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
                            throw new TicketOrderServiceException("Positions are not available");
                        })
                        .flatMap(movieEmission -> movieEmissionRepository.addOrUpdate(movieEmission.removeFreePositions(createPurchaseDto.getTicketsDetails()))
                                .then(principal)
                                .flatMap(val -> userRepository.findByUsername(val.getName()))
                                .map(user -> TicketPurchase.builder()
                                        .purchaseDate(LocalDate.now())
                                        .ticketGroupType(value.getTicketGroupType())
                                        .user(user)
                                        .movieEmission(movieEmission)
                                        .ticketGroupType(createPurchaseDto.getTicketGroupType())
                                        .tickets(value.getTicketsDetails()
                                                .stream()
                                                .map(ticketDetailsDto -> Ticket.builder()
                                                        .position(ticketDetailsDto.getPosition())
                                                        .type(ticketDetailsDto.getIndividualTicketType())
                                                        .ticketStatus(TicketStatus.PURCHASED)
                                                        .discount(createPurchaseDto.getBaseDiscount().add(ticketDetailsDto.getIndividualTicketType().getDiscount()))
                                                        .build())
                                                .collect(Collectors.toList()))
                                        .build())))
                .flatMap(ticketPurchase ->
                        ticketRepository.addOrUpdateMany(ticketPurchase.getTickets())
                                .then(ticketPurchaseRepository.addOrUpdate(ticketPurchase))
                                .map(TicketPurchase::toDto))
                .as(transactionalOperator::transactional);
    }

    public Mono<TicketPurchaseDto> purchaseTicketFromOrder(String username, String ticketOrderId) {

        if (isNull(ticketOrderId)) {
            throw new TicketPurchaseServiceException("Ticket order id is null");
        }

        return ticketOrderRepository.findById(ticketOrderId)
                .map(ticketOrder -> getIfTicketOrderIsUsable(ticketOrder, username))
                .flatMap(ticketOrder ->
                        ticketOrderRepository.addOrUpdate(ticketOrder.changeOrderStatusToDone())
                                .then(ticketPurchaseRepository.addOrUpdate(ticketOrder.toTicketPurchase())))
                .map(TicketPurchase::toDto)
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

    public Flux<TicketPurchaseDto> getAllTicketPurchasesByUser(String username) {

        return ticketPurchaseRepository
                .findAllByUserUsername(username)
                .map(TicketPurchase::toDto);
    }

    public Flux<TicketPurchaseDto> getAllTicketPurchasesByUserAndCity(String username, String cityName) {


        if (isNull(cityName) || StringUtils.isBlank(cityName)) {
            return Flux.error(() -> new TicketPurchaseServiceException("City name is required and cannot be empty"));
        }

        return cityRepository.findByName(cityName)
                .flatMapMany(Flux::just)
                .switchIfEmpty(Flux.error(() -> new TicketPurchaseServiceException("No city with name %s".formatted(cityName))))
                .flatMap(city ->
                        cinemaHallRepository
                                .findAllById(city.getCinemas()
                                        .stream()
                                        .flatMap(cinema -> cinema.getCinemaHalls().stream())
                                        .map(CinemaHall::getId)
                                        .collect(Collectors.toList())))
                .map(CinemaHall::getId)
                .collectList()
                .flatMapMany(cinemaHallsIds -> ticketPurchaseRepository.findAllByUserUsername(username)
                        .collect(
                                ArrayList<TicketPurchase>::new,
                                (list, nextItem) -> cinemaHallsIds
                                        .forEach(id -> {
                                            if (id.equals(nextItem.getMovieEmission().getCinemaHallId())) {
                                                list.add(nextItem);
                                            }
                                        })
                        ))
                .flatMapIterable(Function.identity())
                .map(TicketPurchase::toDto);
    }
}
