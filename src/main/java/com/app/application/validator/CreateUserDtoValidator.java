package com.app.application.validator;

import com.app.application.dto.CreateUserDto;
import com.app.application.validator.generic.Validator;
import org.apache.commons.validator.GenericValidator;
import org.joda.time.format.DateTimeFormat;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
public class CreateUserDtoValidator implements Validator<CreateUserDto> {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Override
    public Map<String, String> validate(CreateUserDto createUserDto) {
        var errors = new HashMap<String, String>();

        if (isNull(createUserDto)) {
            errors.put("dto object:", "is null");
            return errors;
        }

        if (isNull(createUserDto.getPassword()) || isNull(createUserDto.getPasswordConfirmation())) {
            errors.put("Password:", "password and confirmation password must be set");
        } else if (createUserDto.getPassword().length() < 5) {
            errors.put("Password:", "Minimum password length is 5 characters");
        } else if (!createUserDto.getPassword().equals(createUserDto.getPasswordConfirmation())) {
            errors.put("Password:", "Password and confirmation password does not match");
        }

        if (isNull(createUserDto.getPassword()) || isNull(createUserDto.getPasswordConfirmation()) || !createUserDto.getPassword().equals(createUserDto.getPasswordConfirmation())) {
            errors.put("Password:", "is not valid");
        }

        if (isNull(createUserDto.getUsername())) {
            errors.put("Username:", "is null");
        } else if (createUserDto.getUsername().length() < 5) {
            errors.put("Username:", "Minimum length of user is 5 characters");
        }


        if (isNull(createUserDto.getBirthDate())) {
            errors.put("Birth date:", "is null");
        } else if (!GenericValidator.isDate(createUserDto.getBirthDate(), DATE_FORMAT, true)) {
            errors.put("Birth date: %s:".formatted(createUserDto.getBirthDate()), "Birth date format should be: %s".formatted(DATE_FORMAT));
        } else if (LocalDate.now().minusYears(18).compareTo(LocalDate.from(dateTimeFormatter.parse(createUserDto.getBirthDate()))) < 0) {
            errors.put("Birt date:", "User has to be an adult");
        }

        return errors;
    }
}
