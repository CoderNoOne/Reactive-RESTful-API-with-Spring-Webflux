package com.app.infrastructure.mongo.config.converter;

import com.app.domain.vo.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToMoneyConverter implements Converter<String, Money> {

    @Override
    public Money convert(String stringValue) {
        return new Money(stringValue);
    }
}
