package com.app.application.dto;

import com.app.domain.ticket.enums.IndividualTicketType;
import com.app.domain.vo.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TicketDetailsDto {

    private IndividualTicketType individualTicketType;
    private Position position;

}
