package com.app.application.validator;

import com.app.application.dto.CreateTicketOrderDto;
import com.app.application.validator.generic.Validator;
import com.app.application.validator.util.TicketBaseValidationUtils;
import com.app.domain.ticket_order.enums.TicketOrderType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Objects.nonNull;

@Component
public class CreateTicketsOrderDtoValidator implements Validator<CreateTicketOrderDto> {

    @Override
    public Map<String, String> validate(CreateTicketOrderDto item) {
        return validateTicketOrder(TicketBaseValidationUtils.validate(item), item);
    }

    private boolean isTicketOrderTypeValid(TicketOrderType ticketOrderType) {
        return nonNull(ticketOrderType);
    }

    private Map<String, String> validateTicketOrder(Map<String, String> currentErrors, CreateTicketOrderDto item) {

        if (currentErrors.isEmpty() && !isTicketOrderTypeValid(item.getTicketOrderType())) {
            currentErrors.put("ticketOrderType {%s}".formatted(item.getTicketOrderType()), "is not valid");
        }

        return currentErrors;
    }

}
