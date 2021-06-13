package com.leovegas.wallet.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class Amount {

    private final String currencyCode;
    private final BigDecimal value;

    @JsonCreator
    public Amount(@JsonProperty("currencyCode") String currencyCode, @JsonProperty("value") BigDecimal value) {
        super();
        this.currencyCode = currencyCode;
        this.value = value;
    }

    public static final Amount zero(String currencyCode) {
        return new Amount(currencyCode, BigDecimal.ZERO);
    }

    public static final Amount sek(BigDecimal amount) {
        return new Amount("SEK", amount);
    }

    public static final Amount sek(Double amount) {
        return new Amount("SEK", BigDecimal.valueOf(amount));
    }

    public static final Amount of(String currencyCode, Double amount) {
        return new Amount(currencyCode, BigDecimal.valueOf(amount));
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var amount1 = (Amount) o;
        return Objects.equals(currencyCode, amount1.currencyCode) &&
                Objects.equals(value, amount1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, value);
    }
}
