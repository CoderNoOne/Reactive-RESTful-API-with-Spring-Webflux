package com.app.application.validator;

import com.app.application.dto.CreateUserDto;
import com.app.application.validator.generic.Validator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
public class CreateUserDtoValidator implements Validator<CreateUserDto> {

    @Override
    public Map<String, String> validate(CreateUserDto createUserDto) {
        var errors = new HashMap<String, String>();

        if (isNull(createUserDto)) {
            errors.put("dto object", "is null");
            return errors;
        }

        // TODO: 10/18/20
        if (isNull(createUserDto.getPassword()) || isNull(createUserDto.getPasswordConfirmation()) || !createUserDto.getPassword().equals(createUserDto.getPasswordConfirmation())) {
            errors.put("password", "is not valid");
        }

        return errors;
    }
}
