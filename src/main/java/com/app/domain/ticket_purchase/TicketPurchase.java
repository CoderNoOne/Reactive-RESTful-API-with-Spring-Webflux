package com.app.domain.ticket_purchase;

import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.security.User;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket_order.enums.TicketOrderType;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;


@Builder
public class TicketPurchase {

    @Id
    private String id;

    private User user;
    private LocalDate purchaseDate;
    private MovieEmission movieEmission;

    private List<Ticket> tickets;

    @Field("ticket_order_type")
    private TicketOrderType ticketOrderType;
}
