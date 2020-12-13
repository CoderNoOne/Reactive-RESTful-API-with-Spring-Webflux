package com.app.application.validator;

import com.app.application.dto.CreateUserDto;
import com.app.application.validator.generic.Validator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

        if (isNull(createUserDto.getPassword()) || isNull(createUserDto.getPasswordConfirmation()) || !createUserDto.getPassword().equals(createUserDto.getPasswordConfirmation())) {
            errors.put("Password", "is not valid");
        }

        if(isNull(createUserDto.getUsername()) || createUserDto.getUsername().length() < 5){
            errors.put("Username", "Minimum length of user is 5 characters");
        }

        if(isNull(createUserDto.getBirthDate()) || LocalDate.now().minusYears(18).compareTo(createUserDto.getBirthDate()) < 0) {
            errors.put("Birth date", "User has to be adult");
        }

        return errors;
    }
}
