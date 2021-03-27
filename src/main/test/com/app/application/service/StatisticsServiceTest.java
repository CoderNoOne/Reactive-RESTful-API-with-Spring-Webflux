package com.app.application.service;

import com.app.domain.cinema.CinemaRepository;
import com.app.domain.cinema_hall.CinemaHallRepository;
import com.app.domain.city.CityRepository;
import com.app.domain.ticket_order.TicketOrderRepository;
import com.app.domain.ticket_purchase.TicketPurchaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class StatisticsServiceTest {

    @Mock
    private CinemaHallRepository cinemaHallRepository;
    @Mock
    private TicketOrderRepository ticketOrderRepository;
    @Mock
    private TicketPurchaseRepository ticketPurchaseRepository;
    @Mock
    private CinemaRepository cinemaRepository;
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void test1() {


    }

}