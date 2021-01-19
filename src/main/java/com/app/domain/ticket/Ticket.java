package com.app.domain.ticket;

import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.ticket.enums.IndividualTicketType;
import com.app.domain.vo.Discount;
import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;

    private TicketStatus ticketStatus;
    private IndividualTicketType type;
    private MovieEmission movieEmission;
    private Position position;
    private Discount discount;
}

