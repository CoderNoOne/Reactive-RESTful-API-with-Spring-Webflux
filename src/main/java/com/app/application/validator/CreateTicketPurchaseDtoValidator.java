package com.app.application.validator;

import com.app.application.dto.CreateTicketPurchaseDto;
import com.app.application.validator.generic.Validator;
import com.app.application.validator.util.TicketBaseValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreateTicketPurchaseDtoValidator implements Validator<CreateTicketPurchaseDto, String> {

    @Override
    public Map<String, String> validate(CreateTicketPurchaseDto item) {

        return TicketBaseValidationUtils.validate(item);
    }

}
