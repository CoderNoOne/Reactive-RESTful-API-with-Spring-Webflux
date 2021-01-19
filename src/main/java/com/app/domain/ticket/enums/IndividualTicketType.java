package com.app.domain.ticket.enums;

import com.app.domain.vo.Discount;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum IndividualTicketType {
    REGULAR(Discount.of("0.0")),
    STUDENT(Discount.of("0.2"));

    @Getter
    private final Discount discount;
}
