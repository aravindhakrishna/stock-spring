package com.payconiq.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.payconiq.util.CustomAmountSerializer;

import java.util.Currency;

@JsonSerialize(using = CustomAmountSerializer.class)
public class Amount {
    private Double value;
    private Currency currency;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    private int unit;

    public Amount(Double value,Currency currency,int unit){
        this.currency=currency;
        this.value=value;
        this.unit=unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public static Amount Of(Double value){
        return new Amount(value,Currency.getInstance("EUR"),1);
    }
}
