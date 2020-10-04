package com.app.domain.ticket.enums;

import com.app.domain.vo.Discount;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TicketType {
    REGULAR(new Discount("0.0")),
    STUDENT(new Discount("0.2"));

    private final Discount discount;
}
