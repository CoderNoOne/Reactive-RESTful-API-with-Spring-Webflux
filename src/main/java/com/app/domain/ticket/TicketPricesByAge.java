package com.app.domain.ticket;

import com.app.domain.ticket.enums.TicketDiscountPerAgeRange;
import com.app.domain.ticket.enums.TicketType;
import com.app.domain.vo.Discount;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

public interface TicketPricesByAge {

    static BigDecimal getTicketPriceAfterDiscount(Integer age, BigDecimal basePrice, TicketType ticketType) {

        return Optional.ofNullable(age)
                .flatMap(TicketPricesByAge::getDiscount)
                .orElse(new Discount("0"))
                .getValue().multiply(basePrice);
    }

    private static Optional<Discount> getDiscount(Integer age) {

        return Arrays.stream(TicketDiscountPerAgeRange.values())
                .filter(enumVal -> isBetween(age, enumVal))
                .map(TicketDiscountPerAgeRange::getDiscount)
                .findFirst();
    }

    private static boolean isBetween(Integer age, TicketDiscountPerAgeRange ticketDiscountPerAgeRange) {

        return Optional.ofNullable(age)
                .map(value -> value >= ticketDiscountPerAgeRange.getMinAge() && value <= ticketDiscountPerAgeRange.getMaxAge())
                .orElse(false);

    }
}
