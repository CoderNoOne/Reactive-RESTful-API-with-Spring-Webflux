package com.app.domain.vo;

import com.app.domain.exception.DiscountException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Money {

    private BigDecimal value;

    public Money() {this.value = BigDecimal.ZERO;}

    public Money(String value) {this.value = init(value);}

    private Money(BigDecimal value) {this.value = value;}

    public Money add(String value) {
        return new Money(this.value.add(init(value)));
    }

    public Money add(Money money) {
        return new Money(this.value.add(money.value));
    }

    public Money multiply(Integer quantity) {
        return new Money(this.value.multiply(BigDecimal.valueOf(quantity)));
    }

    public Money multiply(String value) {
        return new Money(this.value.multiply(init(value)));
    }

    private static BigDecimal init(String value) {
        if (value == null || !value.matches("\\d+(\\.\\d+)?")) {
            throw new DiscountException("Money value is not correct");
        }
        return new BigDecimal(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
