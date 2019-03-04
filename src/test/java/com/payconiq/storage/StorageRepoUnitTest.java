package com.payconiq.storage;

import com.payconiq.domain.Amount;
import com.payconiq.domain.Stock;
import org.junit.Test;

import javax.validation.constraints.Null;
import java.time.Instant;
import java.util.Arrays;

public class StorageRepoUnitTest {


    private StockStorageRepo stockStorageRepo;

    Stock stock1=new Stock("gold", Amount.Of(2000.0D),1551548467868L);
    Stock stock2=new Stock("platinum",Amount.Of(2500.0D),1551548467868l);
    Stock stock3=new Stock("sliver",Amount.Of(3400.0D), Instant.now().toEpochMilli());
    Stock stock4=new Stock("bitcoin",Amount.Of(1500.0D),Instant.now().toEpochMilli());

    @Test
    public void insertStock(){
        stockStorageRepo=new StockStorageRepo();
        stockStorageRepo.addStock(stock1);
        stockStorageRepo.addStock(stock2);
        stockStorageRepo.addStock(stock3);

        assert (stockStorageRepo.getStocks().size()==3);

        assert (stockStorageRepo.getStocks().contains(stock1));
        assert (stockStorageRepo.getStocks().contains(stock2));
        assert (stockStorageRepo.getStocks().contains(stock3));
        assert (!stockStorageRepo.getStocks().contains(stock4));
    }

    @Test
    public void updateStock(){
        stockStorageRepo=new StockStorageRepo();
        stockStorageRepo.addStock(stock1);
        stockStorageRepo.addStock(stock2);
        int stockId=stockStorageRepo.addStock(stock3);

        Stock stock=stockStorageRepo.findById(stockId);
        stock.setCurrentPrice(Amount.Of(4500.0D));
        assert (stock!=null);
        stockStorageRepo.updateStock(stock);
        assert (stockStorageRepo.findById(stockId)==stock);
    }


    @Test
    public void findStock(){
        stockStorageRepo=new StockStorageRepo();

        assert (stockStorageRepo.getStocks().size()==0);
        int stockId1=stockStorageRepo.addStock(stock1);
        int stockId2=stockStorageRepo.addStock(stock2);

        assert (stockStorageRepo.findById(stockId1)!=null);
        assert (stockStorageRepo.findById(stockId2) !=null);
        assert (stockStorageRepo.findStockByName(stock1.getName())!=null);
        assert (stockStorageRepo.findById(stockId1).getName()=="gold");
    }

    @Test
    public void insertDelete(){
        stockStorageRepo=new StockStorageRepo();
        int stockId1=stockStorageRepo.addStock(stock1);
        int stockId2=stockStorageRepo.addStock(stock2);
        stockStorageRepo.addStock(stock3);

        assert (stockStorageRepo.getStocks().size()==3);
        stockStorageRepo.deleteStock(stock3);
        assert (stockStorageRepo.getStocks().size()==2);
        stockStorageRepo.deleteStock(stock2);
        stockStorageRepo.deleteStock(stock1);
        assert (stockStorageRepo.getStocks().size()==0);
    }
}
