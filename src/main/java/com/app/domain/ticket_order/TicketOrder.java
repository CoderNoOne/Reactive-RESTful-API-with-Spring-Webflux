package com.app.domain.ticket_order;

import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.security.User;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket_order.enums.TicketOrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("ticket_orders")
public class TicketOrder {

    @Id
    private String id;

    private User user;
    private LocalDate dateOrder;
    private MovieEmission movieEmission;

    private List<Ticket> tickets;

    @Field("ticket_order_type")
    private TicketOrderType ticketOrderType;
}



