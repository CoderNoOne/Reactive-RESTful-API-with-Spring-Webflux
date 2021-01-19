package com.app.application.dto;

import com.app.application.dto.contract.TicketDtoMarker;
import com.app.domain.ticket_order.enums.TicketGroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateTicketOrderDto implements TicketDtoMarker {

    private String movieEmissionId;
    private List<TicketDetailsDto> ticketsDetails;
    private TicketGroupType ticketGroupType;

}

