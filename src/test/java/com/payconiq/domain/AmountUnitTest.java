package com.payconiq.domain;

import org.junit.Test;

public class AmountUnitTest {

    @Test
    public void when_called_getCurrency() {
        Amount amt= Amount.Of(20.0D);
        assert (amt.getCurrency().getCurrencyCode().equals("EUR"));
    }

    @Test
    public void when_called_getPrice() {
        Amount amt= Amount.Of(40.0D);
        assert (amt.getValue().equals(40.0D));
    }

    @Test
    public void when_called_getUnit() {
        Amount amt= Amount.Of(20.0D);
        assert (amt.getUnit()==1);
    }
}
