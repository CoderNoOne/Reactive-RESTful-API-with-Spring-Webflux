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
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
    private final CinemaRepository cinemaRepository;
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
        return getAllTicketPurchasesByCityUtility(cityName, ticketPurchaseRepository.findAllByUserUsername(username));
    }

    public Flux<TicketPurchaseDto> getAllTicketPurchasesByCity(String cityName) {
        return getAllTicketPurchasesByCityUtility(cityName, ticketPurchaseRepository.findAll());
    }

    private Flux<TicketPurchaseDto> getAllTicketPurchasesByCityUtility(String cityName, Flux<TicketPurchase> ticketPurchases) {

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
                .flatMapMany(cinemaHallsIds -> ticketPurchases
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

    public Flux<TicketPurchaseDto> getAllTicketPurchaseByCinema(String cinemaId) {

        if (isNull(cinemaId) || StringUtils.isBlank(cinemaId)) {
            return Flux.error(() -> new TicketPurchaseServiceException("Cinema id is required and cannot be empty"));
        }

        return cinemaRepository.findById(cinemaId)
                .switchIfEmpty(Mono.error(() -> new TicketPurchaseServiceException("No cinema with id: %s".formatted(cinemaId))))
                .flatMap(cinema -> ticketPurchaseRepository
                        .findAllByCinemaHallsIds(cinema
                                .getCinemaHalls()
                                .stream()
                                .map(CinemaHall::getId)
                                .collect(Collectors.toList()))
                        .collectList())
                .flatMapMany(Flux::fromIterable)
                .map(TicketPurchase::toDto);
    }

    public Flux<TicketPurchaseDto> getAllTicketPurchasesByCinemaAndUsername(String cinemaId, String username) {
        if (isNull(cinemaId) || StringUtils.isBlank(cinemaId)) {
            return Flux.error(() -> new TicketPurchaseServiceException("Cinema id is required and cannot be empty"));
        }

        return cinemaRepository.findById(cinemaId)
                .switchIfEmpty(Mono.error(() -> new TicketPurchaseServiceException("No cinema with id: %s".formatted(cinemaId))))
                .flatMap(cinema -> ticketPurchaseRepository
                        .findAllByCinemaHallsIdsAndUsername(cinema
                                .getCinemaHalls()
                                .stream()
                                .map(CinemaHall::getId)
                                .collect(Collectors.toList()), username)
                        .collectList())
                .flatMapMany(Flux::fromIterable)
                .map(TicketPurchase::toDto);
    }

    public Flux<TicketPurchaseDto> getAllTicketPurchases() {
        return ticketPurchaseRepository
                .findAll()
                .map(TicketPurchase::toDto);
    }

    private boolean compareDates(LocalDate from, LocalDate to) {
        return from.compareTo(to) > 0;
    }

    public Flux<TicketPurchaseDto> getAllTicketPurchasesByDate(Optional<String> from, Optional<String> to) {

        var result = new AtomicReference<Flux<TicketPurchaseDto>>(Flux.empty());
        var toDateReference = new AtomicReference<LocalDate>();
        var fromDateReference = new AtomicReference<LocalDate>();

        from.ifPresentOrElse(
                fromDateString -> {
                    boolean isValidFrom = GenericValidator.isDate(fromDateString, "dd-MM-yyyy", true);

                    to.ifPresentOrElse(toDateString -> {
                                boolean isValidTo = GenericValidator.isDate(toDateString, "dd-MM-yyyy", true);

                                result.set(isValidFrom && isValidTo ?
                                        compareDates(
                                                fromDateReference.accumulateAndGet(LocalDate.parse(fromDateString, DateTimeFormatter.ofPattern("dd-MM-yyyy")), (oldVal, newVal) -> newVal),
                                                toDateReference.accumulateAndGet(LocalDate.parse(toDateString, DateTimeFormatter.ofPattern("dd-MM-yyyy")), (oldVal, newVal) -> newVal)) ?
                                                Flux.error(() -> new TicketPurchaseServiceException("From date cannot be after to date!")) :
                                                ticketPurchaseRepository.findAllByPurchaseDateBetween(fromDateReference.get(), toDateReference.get()).map(TicketPurchase::toDto)
                                        : !isValidFrom && !isValidTo ? Flux.error(() -> new TicketPurchaseServiceException("Date from and date to has not valid format")) :
                                        !isValidFrom ? Flux.error(() -> new TicketPurchaseServiceException("Date from has not valid format")) : Flux.error(() -> new TicketPurchaseServiceException("Date to has not valid format")));

                            }
                            , () ->
                                    result.set(!isValidFrom ?
                                            Flux.error(() -> new TicketPurchaseServiceException("Date from has not valid format")) :
                                            ticketPurchaseRepository.findAllByPurchaseDateAfter(fromDateReference.get()).map(TicketPurchase::toDto)));
                },
                () ->
                        result.set(to.isPresent() && !GenericValidator.isDate(to.get(), "dd-MM-yyyy", true) ?
                                Flux.error(() -> new TicketPurchaseServiceException("Date to has not valid format")) :
                                to.isEmpty() ? Flux.error(() -> new TicketPurchaseServiceException("At least one of dates [from, to] is required!")) :
                                        ticketPurchaseRepository.findAllByPurchaseDateBefore(toDateReference.get()).map(TicketPurchase::toDto))
        );


        return result.get();
    }



}
