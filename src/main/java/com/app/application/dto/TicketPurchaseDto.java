package com.app.application.dto;

import com.app.domain.ticket_order.enums.TicketGroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TicketPurchaseDto {

    private String id;
    private String username;
    private LocalDate purchaseDate;
    private MovieEmissionDto movieEmissionDto;

    private List<TicketDto> tickets;

    @Field("ticket_order_type")
    private TicketGroupType ticketGroupType;
}
