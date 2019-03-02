package com.payconiq.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.payconiq.domain.Amount;

import java.io.IOException;

public class CustomAmountSerializer extends JsonSerializer<Amount> {

    @Override
    public void serialize(Amount amount, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("currency",amount.getCurrency().getCurrencyCode());
        jsonGenerator.writeNumberField("amount",amount.getValue());
        jsonGenerator.writeNumberField("unit",amount.getUnit());
        jsonGenerator.writeEndObject();
    }
}
