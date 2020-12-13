package com.app.domain.vo;

import com.app.domain.exception.DiscountException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Discount {
    private BigDecimal value;

    public Discount() {this.value = BigDecimal.ZERO;}

    public Discount(String value) {this.value = init(value);}

    private Discount(BigDecimal value) {this.value = value;}

    public Discount inverse() {
        return new Discount(BigDecimal.ONE.subtract(value));
    }

    private static BigDecimal init(String value) {
        if (value == null || !value.matches("\\d\\.\\d+")) {
            throw new DiscountException("discount value is not correct");
        }

        var decimalValue = new BigDecimal(value);
        if (decimalValue.compareTo(BigDecimal.ZERO) < 0 || decimalValue.compareTo(BigDecimal.ONE) > 0) {
            throw new DiscountException("discount value is out of range");
        }

        return decimalValue;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
