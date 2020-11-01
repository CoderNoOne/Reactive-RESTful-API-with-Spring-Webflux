package com.app.application.dto;

import com.app.domain.ticket_order.enums.TicketOrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateTicketOrderDto {

    private String movieEmissionId;
    private List<TicketDetailsDto> ticketsDetails;
    private TicketOrderType ticketOrderType;

}

