package com.app.application.validator;

import com.app.application.dto.CreateTicketOrderDto;
import com.app.application.dto.TicketDetailsDto;
import com.app.application.validator.generic.Validator;
import com.app.domain.ticket_order.enums.TicketOrderType;
import com.app.domain.vo.Position;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class CreateTicketsOrderDtoValidator implements Validator<CreateTicketOrderDto> {

    @Override
    public Map<String, String> validate(CreateTicketOrderDto item) {

        var errors = new HashMap<String, String>();

        if (isNull(item)) {
            errors.put("dto object", "is null");
            return errors;
        }

        if (!isMovieEmissionIdValid(item.getMovieEmissionId())) {
            errors.put("movieEmissionId {%s}".formatted(item.getMovieEmissionId()), "is not valid");
        }

        if (!areTicketDetailsValid(item.getTicketsDetails())) {
            errors.put("ticketDetails {%s}".formatted(item.getTicketsDetails()), "are not valid");
        }

        if (!isTicketOrderTypeValid(item.getTicketOrderType())) {
            errors.put("ticketOrderType {%s}".formatted(item.getTicketOrderType()), "is not valid");
        }

        return errors;
    }

    private boolean isTicketOrderTypeValid(TicketOrderType ticketOrderType) {
        return nonNull(ticketOrderType);
    }

    private boolean areTicketDetailsValid(List<TicketDetailsDto> ticketDetails) {
        return nonNull(ticketDetails) && !ticketDetails.isEmpty()
                && arePositionsUnique(ticketDetails.stream().map(TicketDetailsDto::getPosition).collect(Collectors.toList()))
                && ticketDetails.stream().allMatch(this::isSingleTicketDetailValid);
    }

    private boolean isSingleTicketDetailValid(TicketDetailsDto ticketDetailsDto) {
        return nonNull(ticketDetailsDto) && ticketDetailsDto.getViewerAge() >= 1 && isPositionValid(ticketDetailsDto.getPosition());
    }

    private boolean isPositionValid(Position position) {
        return nonNull(position) && nonNull(position.getColNo()) && position.getColNo() >= 1
                && nonNull(position.getRowNo()) && position.getRowNo() >= 1;
    }

    private boolean isMovieEmissionIdValid(String movieEmissionId) {
        return StringUtils.isNotBlank(movieEmissionId);
    }

    private boolean arePositionsUnique(List<Position> positions) {

        return positions.stream()
                .distinct().count() == positions.size();
    }
}
