package com.app.domain.ticket.enums;

import com.app.domain.vo.Discount;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TicketDiscountPerAgeRange {

    FROM_1_TO_5(0, 5, new Discount("0.5")),
    FROM_6_TO_13(6, 13, new Discount("0.3")),
    FROM_14_TO_18(14, 18, new Discount("0.2")),
    FROM_19_TO_26(19, 26, new Discount("0.1"));

    private final Integer minAge;
    private final Integer maxAge;
    private final Discount discount;

}
