package com.payconiq.util;

public class NotSupportedCurrencyException extends RuntimeException {
    public NotSupportedCurrencyException(String err){
        super(err);
    }
}
