package com.app.domain.ticket;

import com.app.domain.MovieEmission;
import com.app.domain.security.User;
import com.app.domain.ticket.enums.TicketType;
import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;

    private User user;
    private TicketType type;
    private MovieEmission movieEmission;
    private Position position;

    public BigDecimal ticketPrice() {
        return TicketPricesByAge.getTicketPriceAfterDiscount(
                user.getAge(),
                movieEmission.getMovie().getPrice().getValue(),
                type);
    }

}

