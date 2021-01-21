package com.app.application.validator;

import com.app.application.dto.CreateCinemaDto;
import com.app.application.validator.generic.Validator;
import com.app.domain.cinema_hall.CinemaHall;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

public class CreateCinemaDtoValidator implements Validator<CreateCinemaDto, String> {

    @Override
    public Map<String, String> validate(CreateCinemaDto item) {

        var errors = new HashMap<String, String>();

        if (isNull(item)) {
            errors.put("dt object", "is null");
            return errors;
        }

        if(isBlank(item.getCity())){
            errors.put("City name", "is blank");
        }


        return errors;
    }


}
