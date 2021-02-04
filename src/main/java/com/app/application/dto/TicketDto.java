package com.app.application.dto;

import com.app.domain.ticket.enums.IndividualTicketType;
import com.app.domain.ticket.enums.TicketStatus;
import com.app.domain.vo.Discount;
import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TicketDto {

    private String id;

    private TicketStatus ticketStatus;
    private IndividualTicketType type;
    private Position position;
    private Discount discount;
}
