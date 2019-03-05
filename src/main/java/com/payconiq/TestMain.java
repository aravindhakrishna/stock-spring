package com.payconiq;

import com.payconiq.domain.Amount;

import java.text.NumberFormat;
import java.time.Instant;
import java.util.Locale;

public class TestMain {

    public static void main(String[] args){
        Amount amount=Amount.Of(2500.D);
        NumberFormat currencyFormatter=NumberFormat.getCurrencyInstance(Locale.GERMANY);

        System.out.println(amount.getCurrency().getCurrencyCode()+"----"+amount.getCurrency().getDisplayName()+"-----"+amount.getCurrency().getSymbol());
        System.out.println(currencyFormatter.format(amount.getValue()));
        long ts=1551780600000L/1000;//2019-03-02T15:58:55.998Z
        System.out.println(Instant.parse("2014-09-06T19:00:00.000Z"));
    }
}
