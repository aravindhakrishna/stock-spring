package com.payconiq.storage;

import com.payconiq.domain.Stock;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class StockStorageRepo {
    private List<Stock> stocks= new CopyOnWriteArrayList<Stock>();
    private volatile  int stockId=100;

    public synchronized  int addStock(Stock stock){
        stock.setId(stockId);
        stocks.add(stock);
        stockId++;
        return stock.getId();
    }

    public Stock findStockByTimeAndName(String name,Long ts){
        for(int i=0;i<stocks.size();i++){
            if(stocks.get(i).getName().equals(name) && stocks.get(i).getLastUpdate()==ts) {
                Stock stock=stocks.get(i);
                stock.setIndex(i);
                return  stock;
            }
        }
        return null;
    }

    public Stock findStockByName(String name){
         for(int i=0;i<stocks.size();i++){
             if(stocks.get(i).getName().equals(name)) {
                 Stock stock=stocks.get(i);
                 stock.setIndex(i);
                 return  stock;
             }
         }
        return null;
    }

    public Stock findById(int id){
        for(int i=0;i<stocks.size();i++){
            if(stocks.get(i).getId()==id) {
                Stock stock=stocks.get(i);
                stock.setIndex(i);
                return  stock;
            }
        }
        return null;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void deleteStock(Stock stock){
        stocks.remove(stock);
    }

    public void updateStock(Stock stock){
        stocks.set(stock.getIndex(),stock);
    }

}
