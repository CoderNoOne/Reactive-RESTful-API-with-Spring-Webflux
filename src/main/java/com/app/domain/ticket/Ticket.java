package com.app.domain.ticket;

import com.app.application.dto.TicketDto;
import com.app.domain.generic.GenericEntity;
import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.ticket.enums.IndividualTicketType;
import com.app.domain.vo.Discount;
import com.app.domain.vo.Money;
import com.app.domain.vo.Position;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tickets")
public class Ticket implements GenericEntity {

    @Id
    private String id;

    private TicketStatus ticketStatus;
    private IndividualTicketType type;

    private Position position;
    private Discount discount;
    @Getter
    private Money price;

    public TicketDto toDto() {
        return TicketDto.builder()
                .id(id)
                .position(position)
                .discount(discount)
                .type(type)
                .ticketStatus(ticketStatus)
                .price(price)
                .build();
    }
}

