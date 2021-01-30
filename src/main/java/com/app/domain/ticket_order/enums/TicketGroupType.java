package com.app.domain.ticket_order.enums;

import com.app.domain.vo.Discount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TicketGroupType {
    FAMILY(Discount.of("0.2")),
    NORMAL(Discount.of("0.0"));

    @Getter
    private final Discount discount;
}
