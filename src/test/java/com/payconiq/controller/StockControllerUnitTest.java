package com.payconiq.controller;

import com.payconiq.domain.Amount;
import com.payconiq.domain.Stock;
import com.payconiq.storage.StockStorageRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@WebMvcTest(value = StockController.class,secure = false)
public class StockControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  StockStorageRepo stockStorageRepo;

    Stock stock1=new Stock("gold", Amount.Of(2000.0D),1551548467868L);
    Stock stock2=new Stock("platinum",Amount.Of(2500.0D),1551548467868l);
    Stock stock3=new Stock("sliver",Amount.Of(3500.0D),Instant.now().toEpochMilli());
    Stock stock4=new Stock("bitcoin",Amount.Of(1200.0D),Instant.now().toEpochMilli());
    java.util.List<Stock> stocks=new ArrayList<Stock>();


    @Test
    public void retrieveStocks() throws  Exception{
        stocks.add(stock1);
        stocks.add(stock2);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/stocks");

        Mockito.when(stockStorageRepo.getStocks()).thenReturn(stocks);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "[{id:0,name:GOLD,currentPrice:{\"amount\":2000.0,\"currency\":EUR,\"unit\":1},lastUpdate:1551548467868},{id:0,name:PLATINUM,currentPrice:{\"amount\":2500.0,\"currency\":EUR,\"unit\":1},lastUpdate:1551548467868}]";
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void getStock() throws  Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/stocks/0");
        Mockito.when(stockStorageRepo.findById(Mockito.anyInt())).thenReturn(stock1);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "{id:0,name:GOLD,currentPrice:{\"amount\":2000.0,\"currency\":EUR,\"unit\":1},lastUpdate:1551548467868}";

        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void getStockFailure() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/stocks/1");
        Mockito.when(stockStorageRepo.findById(Mockito.anyInt())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "{\"error\":\"No Match Found\"}";

        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void addStocks() throws  Exception{

        String stock1="{\"currentPrice\":{\"amount\":2000.0,\"currency\":\"EUR\",\"unit\":1},\"name\":\"gold\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/api/stocks")
                .accept(MediaType.APPLICATION_JSON).content(stock1)
                .contentType(MediaType.APPLICATION_JSON);
        Mockito.when(stockStorageRepo.findStockByName(Mockito.anyString())).thenReturn(null);
        Mockito.when(stockStorageRepo.addStock(Mockito.any())).thenReturn(100);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "{\"result\":\"created\",\"id\":100}";

        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }
    @Test
    public void updateStocks() throws  Exception{

        String egStock1="{\"currentPrice\":{\"amount\":2500.0,\"currency\":\"EUR\",\"unit\":1},\"name\":\"gold\",\"lastUpdate\":\"2019-03-02T15:58:55.998Z\"}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/api/stocks")
                .accept(MediaType.APPLICATION_JSON).content(egStock1)
                .contentType(MediaType.APPLICATION_JSON);
        Mockito.when(stockStorageRepo.findStockByName(Mockito.anyString())).thenReturn(Arrays.asList(stock1));
        stock1.setCurrentPrice(Amount.Of(2500.0D));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "{\"result\":\"updated\",\"stock\":{id:0,name:GOLD,currentPrice:{\"amount\":2500.0,\"currency\":\"EUR\",\"unit\":1},lastUpdate:1551542335998}}";
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }
    @Test
    public void  updateStockPrice() throws  Exception{
        String exampleJson="{\"currentPrice\":{\"amount\":2500.0,\"currency\":\"EUR\",\"unit\":1}}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/api/stocks/0")
                .accept(MediaType.APPLICATION_JSON).content(exampleJson)
                .contentType(MediaType.APPLICATION_JSON);
        Mockito.when(stockStorageRepo.findById(Mockito.anyInt())).thenReturn(stock1);
        stock1.setCurrentPrice(Amount.Of(2500.0D));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "{\"result\":\"priceUpdated\",\"stock\":{id:0,name:GOLD,currentPrice:{\"amount\":2500.0,\"currency\":\"EUR\",\"unit\":1},lastUpdate:1551548467868}}";

        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void  updateStockPriceFailure() throws  Exception{
        String exampleJson="{\"currentPrice\":{\"amount\":2500.0,\"currency\":\"EUR\",\"unit\":1}}";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/api/stocks/1")
                .accept(MediaType.APPLICATION_JSON).content(exampleJson)
                .contentType(MediaType.APPLICATION_JSON);
        Mockito.when(stockStorageRepo.findById(Mockito.anyInt())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "{\"error\":\"No Match Found\"}";

        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void deleteStock() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/api/stocks/0");
        Mockito.when(stockStorageRepo.findById(Mockito.anyInt())).thenReturn(stock1);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "{\"result\":\"deleted\"}";

        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void deleteStockFaliure() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/api/stocks/1");
        Mockito.when(stockStorageRepo.findById(Mockito.anyInt())).thenReturn(null);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected= "{\"error\":\"No Match Found\"}";

        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }
}
