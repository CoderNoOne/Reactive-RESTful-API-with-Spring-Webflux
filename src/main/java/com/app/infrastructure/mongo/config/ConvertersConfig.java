package com.app.infrastructure.mongo.config;

import com.app.infrastructure.mongo.config.converter.LocalDateToStringConverter;
import com.app.infrastructure.mongo.config.converter.StringToLocalDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class ConvertersConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {

        return new MongoCustomConversions(
                List.of(
                        new LocalDateToStringConverter(),
                        new StringToLocalDateConverter()));
    }
}

