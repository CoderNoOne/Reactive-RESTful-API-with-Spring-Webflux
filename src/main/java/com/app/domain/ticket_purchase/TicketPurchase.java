package com.app.domain.ticket_purchase;

import com.app.application.dto.TicketPurchaseDto;
import com.app.domain.movie_emission.MovieEmission;
import com.app.domain.security.User;
import com.app.domain.ticket.Ticket;
import com.app.domain.ticket_order.enums.TicketGroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("ticket_purchases")
public class TicketPurchase {

    @Id
    @Getter
    private String id;

    private User user;
    private LocalDate purchaseDate;

    @Getter
    private MovieEmission movieEmission;
    @Getter
    private List<Ticket> tickets;

    @Field("ticket_order_type")
    private TicketGroupType ticketGroupType;

    public TicketPurchaseDto toDto() {
        return TicketPurchaseDto.builder()
                .id(id)
                .username(user.getUsername())
                .movieEmissionDto(movieEmission.toDto())
                .purchaseDate(purchaseDate)
                .tickets(tickets.stream().map(Ticket::toDto).collect(Collectors.toList()))
                .ticketGroupType(ticketGroupType)
                .build();
    }
}

