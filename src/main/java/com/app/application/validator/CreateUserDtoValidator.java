package com.app.application.validator;

import com.app.application.dto.CreateUserDto;
import com.app.application.validator.generic.Validator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CreateUserDtoValidator implements Validator<CreateUserDto> {

    @Override
    public Map<String, String> validate(CreateUserDto createUserDto) {
        var errors = new HashMap<String, String>();

        if (Objects.isNull(createUserDto)) {
            errors.put("object", "is null");
            return errors;
        }

        if (!(Objects.equals(createUserDto.getPassword(), createUserDto.getPasswordConfirmation()))) {
            errors.put("password", "is not valid");
        }

        return errors;
    }
}
