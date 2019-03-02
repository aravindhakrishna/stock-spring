package com.payconiq.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.domain.Amount;
import com.payconiq.domain.Stock;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CustomStockUnitTest {

    @Test
    public void deserialize() throws IOException{
        String stock1="{\"currentPrice\":{\"amount\":2000.0,\"currency\":\"EUR\",\"unit\":1},\"name\":\"gold\"}";

        Stock stock=new ObjectMapper().readValue(stock1,Stock.class);
        assert (stock.getName().equals("gold"));;
        assert (stock.getCurrentPrice().getCurrency().getCurrencyCode().equals("EUR"));
        assert (stock.getCurrentPrice().getUnit()==1);
    }

    @Test
    public void deserializeError() throws IOException{
        String stock1="{\"currentPrice\":{\"amout\":2000.0,\"currency\":\"EUR\",\"unit\":1},\"name\":\"gold\"}";
        try {
            new ObjectMapper().readValue(stock1, Stock.class);
        }catch (Exception exe){
            assert(exe.getMessage()==null);
        }

        String stock2="{\"currentPrice\":{\"amount\":2000.0,\"currency\":\"USD\",\"unit\":1},\"name\":\"gold\"}";
        try {
            new ObjectMapper().readValue(stock2, Stock.class);
        }catch (Exception exe){
            assertThat(exe.getMessage(),is("Currency Not supported"));
        }
    }
}
