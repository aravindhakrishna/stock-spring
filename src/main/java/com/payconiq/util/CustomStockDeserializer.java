package com.payconiq.util;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.payconiq.domain.Amount;
import com.payconiq.domain.Stock;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Currency;

public class CustomStockDeserializer extends JsonDeserializer<Stock> {
    @Override
    public Stock deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        try{
            Double amount=node.get("currentPrice").get("amount").asDouble();
            String currency=node.get("currentPrice").get("currency").asText();
            int unit=node.get("currentPrice").get("unit").asInt();
            Instant ts=null;
            if(node.has("lastUpdate")){
                ts=Instant.parse(node.get("lastUpdate").asText());
            }else{
               ts=Instant.now(Clock.systemUTC());
            }
            if(node.has("name")){
                String name=node.get("name").asText();
                return  new Stock(name,covertAmount(amount,currency,unit) ,ts.toEpochMilli());
            }else{
                return  new Stock(covertAmount(amount,currency,unit),ts.toEpochMilli());
            }

        }catch (NumberFormatException ex){
            throw ex;
        }catch (DateTimeParseException exe){
            throw exe;
        }catch (NullPointerException ne){
            throw ne;
        }
    }

    public Amount covertAmount(Double amount, String currency,int unit){
            Currency currency1=Currency.getInstance(currency);
            if(!currency1.getCurrencyCode().equals("EUR")){
                throw new NotSupportedCurrencyException("Currency Not supported");
            }
            return  new Amount(amount,currency1,unit);
    }
}
