package com.app.application.dto.contract;

import com.app.application.dto.TicketDetailsDto;
import com.app.domain.ticket_order.enums.TicketGroupType;
import com.app.domain.vo.Discount;
import com.app.domain.vo.Position;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface TicketDtoMarker {

    String getMovieEmissionId();

    List<TicketDetailsDto> getTicketsDetails();

    TicketGroupType getTicketGroupType();

    default boolean areAllPositionsAvailable(List<Position> freePositions) {
        return getTicketsDetails().stream()
                .map(TicketDetailsDto::getPosition)
                .allMatch(freePositions::contains);
    }

    @JsonIgnore
    default Discount getBaseDiscount() {
        return getTicketGroupType().getDiscount();
    }

}
