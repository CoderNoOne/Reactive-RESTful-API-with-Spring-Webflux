package com.app.application.dto.contract;

import com.app.application.dto.TicketDetailsDto;
import com.app.domain.ticket_order.enums.TicketOrderType;

import java.util.List;

public interface TicketDtoMarker {
    String getMovieEmissionId();
    List<TicketDetailsDto> getTicketsDetails();
    TicketOrderType getTicketOrderType();
}
