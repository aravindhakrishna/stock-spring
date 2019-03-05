package com.payconiq;

import com.payconiq.domain.Amount;

import java.text.NumberFormat;
import java.util.Locale;

public class TestMain {

    public static void main(String[] args){
        Amount amount=Amount.Of(2500.D);
        NumberFormat currencyFormatter=NumberFormat.getCurrencyInstance(Locale.GERMANY);

        System.out.println(amount.getCurrency().getCurrencyCode()+"----"+amount.getCurrency().getDisplayName()+"-----"+amount.getCurrency().getSymbol());
        System.out.println(currencyFormatter.format(amount.getValue()));
    }
}
