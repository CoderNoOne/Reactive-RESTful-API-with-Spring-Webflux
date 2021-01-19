package com.app.domain.ticket_purchase;

import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.security.User;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket_order.enums.TicketGroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Document("ticket_purchases")
@Builder
public class TicketPurchase {

    @Id
    private String id;

    private User user;
    private LocalDate purchaseDate;
    private MovieEmission movieEmission;

    private List<Ticket> tickets;

    @Field("ticket_order_type")
    private TicketGroupType ticketGroupType;
}