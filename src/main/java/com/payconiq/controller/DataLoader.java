package com.payconiq.controller;

import com.payconiq.domain.Amount;
import com.payconiq.domain.Stock;
import com.payconiq.storage.StockStorageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;

@Component
public class DataLoader {

    @Autowired
    private StockStorageRepo stockStorageRepo;


    @PostConstruct
    public void loadData(){
        Stock stock1=new Stock("gold", Amount.Of(2000.0D),1551548467868L);
        Stock stock2=new Stock("platinum",Amount.Of(2500.0D),1551548467868l);
        Stock stock3=new Stock("sliver",Amount.Of(3400.0D), Instant.now().toEpochMilli());
        Stock stock4=new Stock("bitcoin",Amount.Of(1500.0D),Instant.now().toEpochMilli());
        stockStorageRepo.addStock(stock1);
        stockStorageRepo.addStock(stock2);
        stockStorageRepo.addStock(stock3);
        stockStorageRepo.addStock(stock4);
    }


    @PreDestroy
    public void removeData(){
        stockStorageRepo.clear();
    }
}
