package com.app.application.dto;

import com.app.application.dto.contract.TicketDtoMarker;
import com.app.domain.ticket_order.enums.TicketOrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateTicketPurchaseDto implements TicketDtoMarker {

    private String movieEmissionId;
    private List<TicketDetailsDto> ticketsDetails;
    private TicketOrderType ticketOrderType;
}
