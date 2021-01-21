package com.app.application.validator;

import com.app.application.dto.CreateMailDto;
import com.app.application.validator.generic.Validator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
public class CreateMailDtoValidator implements Validator<CreateMailDto> {

    private final EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public Map<String, String> validate(CreateMailDto item) {

        var errors = new HashMap<String, String>();

        if (isNull(item)) {
            errors.put("dto object: ", "is null");
            return errors;
        }

        if (!isEmailValid(item.getTo())) {
            errors.put("To email: %s :".formatted(item.getTo()), "is not valid");
        }

        if (!isHtmlContentValid(item.getHtmlContent())) {
            errors.put("Html Content:", "is not valid");
        }

        if (!isTittleValid(item.getTitle())) {
            errors.put("Title: %s : ".formatted(item.getTitle()), "is not valid");
        }

        return errors;
    }

    private boolean isEmailValid(String mail) {
        return emailValidator.isValid(mail);
    }

    private boolean isHtmlContentValid(String htmlContent) {
        return Strings.isNotBlank(htmlContent);
    }

    private boolean isTittleValid(String title) {
        return Strings.isNotBlank(title);
    }


}
