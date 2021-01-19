package com.app.domain.ticket;

import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.ticket.enums.TicketType;
import com.app.domain.vo.Money;
import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;

    private TicketStatus ticketStatus;
    private TicketType type;
    private MovieEmission movieEmission;
    private Position position;
    private Money price;

}

