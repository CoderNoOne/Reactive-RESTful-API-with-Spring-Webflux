package com.app.application.dto;

import com.app.domain.ticket_order.enums.TicketGroupType;
import com.app.domain.ticket_order.enums.TicketOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TicketOrderDto {

    private String id;
    private String username;
    private LocalDate orderDate;
    private TicketOrderStatus ticketOrderStatus;
    private MovieEmissionDto movieEmissionDto;
    private List<TicketDto> tickets;
    private TicketGroupType ticketGroupType;
}
