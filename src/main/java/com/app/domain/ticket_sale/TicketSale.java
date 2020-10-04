package com.app.domain.ticket_sale;

import com.app.domain.ticket.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document("ticket_sales")
public class TicketSale {

    @Id
    private String id;

    private List<Ticket> tickets;
}
