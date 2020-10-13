package com.app.domain.ticket;

import com.app.domain.ticket.enums.TicketDiscountPerAgeRange;
import com.app.domain.ticket.enums.TicketType;
import com.app.domain.vo.Discount;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

public interface TicketPrices {

    static BigDecimal getTicketPriceAfterDiscount(Integer age, BigDecimal basePrice, TicketType ticketType) {

        return Optional.ofNullable(age)
                .map(TicketPrices::getDiscountRegardingAge)
                .map(Discount::inverse)
                .map(discAge -> discAge.getValue()
                        .multiply(getDiscountRegardingTicketType(ticketType).getValue())
                        .multiply(basePrice))
                .orElse(basePrice);
    }

    private static Discount getDiscountRegardingTicketType(TicketType ticketType) {

        return Optional
                .ofNullable(ticketType)
                .map(TicketType::getDiscount)
                .orElse(new Discount("0"));
    }

    private static Discount getDiscountRegardingAge(Integer age) {

        return Arrays.stream(TicketDiscountPerAgeRange.values())
                .filter(enumVal -> isBetween(age, enumVal))
                .map(TicketDiscountPerAgeRange::getDiscount)
                .findFirst()
                .orElse(new Discount("0"));
    }

    private static boolean isBetween(Integer age, TicketDiscountPerAgeRange ticketDiscountPerAgeRange) {

        return Optional.ofNullable(age)
                .map(value -> value >= ticketDiscountPerAgeRange.getMinAge() && value <= ticketDiscountPerAgeRange.getMaxAge())
                .orElse(false);

    }
}
