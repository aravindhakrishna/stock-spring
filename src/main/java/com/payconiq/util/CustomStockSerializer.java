package com.payconiq.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.payconiq.domain.Stock;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class CustomStockSerializer extends JsonSerializer<Stock> {
    @Override
    public void serialize(Stock stock, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        DateTimeFormatter dtf= DateTimeFormatter.ofPattern("YYYY-MM-DD'T'hh:mm:ss.SSSZ");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id",stock.getId());
        jsonGenerator.writeStringField("name",stock.getName());
        jsonGenerator.writeObjectField("currentPrice",stock.getCurrentPrice());
//        Instant.ofEpochMilli(stock.getLastUpdate());
        jsonGenerator.writeNumberField("lastUpdate",stock.getLastUpdate());
        jsonGenerator.writeEndObject();
    }
}
