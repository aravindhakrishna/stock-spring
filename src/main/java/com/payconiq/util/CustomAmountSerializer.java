package com.payconiq.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.payconiq.domain.Amount;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class CustomAmountSerializer extends JsonSerializer<Amount> {

    @Override
    public void serialize(Amount amount, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        NumberFormat currencyFormatter=NumberFormat.getCurrencyInstance(Locale.GERMANY);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("currency",amount.getCurrency().getSymbol());
        jsonGenerator.writeNumberField("amount",amount.getValue());
        jsonGenerator.writeNumberField("unit",amount.getUnit());
        jsonGenerator.writeStringField("formatted",currencyFormatter.format(amount.getValue()));
        jsonGenerator.writeEndObject();
    }
}
