package com.payconiq.domain;

import com.payconiq.domain.Stock;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;

public class StockUnitTest {


    @Test
    public void when_called_getName() {
        Stock stock = new Stock("gold", Amount.Of(20.0D), Instant.now(Clock.systemUTC()).toEpochMilli());
        assert (stock.getName().equals("gold"));
    }

    @Test
    public void when_called_getPrice() {
        Stock stock = new Stock("gold", Amount.Of(40.0D), Instant.now(Clock.systemUTC()).toEpochMilli());
        assert (stock.getCurrentPrice().getValue().equals(40.0D));
    }

    @Test
    public void when_called_getlastUpdateTime() {
        Long time= Instant.now(Clock.systemUTC()).toEpochMilli();
        Stock stock = new Stock("gold", Amount.Of(50.0D), time);
        assert (stock.getLastUpdate().equals(time));
    }
}

